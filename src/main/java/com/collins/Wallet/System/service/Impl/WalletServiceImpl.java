package com.collins.Wallet.System.service.Impl;

import com.collins.Wallet.System.dtos.ResponseDto;
import com.collins.Wallet.System.dtos.ResponseDtos.TransferRespDto;
import com.collins.Wallet.System.dtos.ResquestDto.CreateUserRequestDto;
import com.collins.Wallet.System.dtos.ResquestDto.DoTransDto;
import com.collins.Wallet.System.dtos.ResquestDto.FundAccountRequestDto;
import com.collins.Wallet.System.enums.AccountStatus;
import com.collins.Wallet.System.enums.IdempotencyStatus;
import com.collins.Wallet.System.event.FundAccountCreatedEvent;
import com.collins.Wallet.System.event.UserCreatedEvent;
import com.collins.Wallet.System.exception.*;
import com.collins.Wallet.System.mapper.UserMapper;
import com.collins.Wallet.System.mapper.WalletMapper;
import com.collins.Wallet.System.model.Account;
import com.collins.Wallet.System.model.IdempotencyKey;
import com.collins.Wallet.System.model.Users;
import com.collins.Wallet.System.model.WalletBalance;
import com.collins.Wallet.System.repository.AccountRepository;
import com.collins.Wallet.System.repository.IdempotencyRepository;
import com.collins.Wallet.System.repository.UserRepository;
import com.collins.Wallet.System.repository.WalletBalanceRepository;
import com.collins.Wallet.System.service.WalletService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final WalletBalanceRepository walletBalanceRepository;
    private final IdempotencyRepository idempotencyRepository;
    private final ApplicationEventPublisher publisher;



    @Transactional
    @Override
    public ResponseEntity<ResponseDto> createUser(CreateUserRequestDto createUserRequestDto) {

        log.info("Creating wallet user with email {}", createUserRequestDto.getEmail());

        userRepository.findByEmail(createUserRequestDto.getEmail())
                .ifPresent(user -> {
                    throw new UserAlreadyExistException("Email already registered");
                });

        Users user = UserMapper.mapToUser(createUserRequestDto);
        user = userRepository.save(user);
        publisher.publishEvent(new UserCreatedEvent(user));

        log.info("User {} successfully", createUserRequestDto.getEmail());

        ResponseDto response = new ResponseDto(
                "201",
                "User created successfully with wallet account"
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @Transactional
    @Override
    public ResponseEntity<TransferRespDto> transfer(DoTransDto transferRequest, String idempotencyKey) {

        log.info("Processing transfer request with idempotency key {}", idempotencyKey);

        validateIdempotency(idempotencyKey);

        Account sourceAccount = getAccount(transferRequest.getSourceAccount());
        Account destinationAccount = getAccount(transferRequest.getDestinationAccount());

        validateAccountStatus(sourceAccount);
        validateAccountStatus(destinationAccount);

        WalletBalance sourceWallet = getWallet(sourceAccount);
        WalletBalance destinationWallet = getWallet(destinationAccount);

        validateBalance(sourceWallet.getBalance(), transferRequest.getAmount());

        performTransfer(sourceWallet, destinationWallet, transferRequest.getAmount());

        walletBalanceRepository.save(sourceWallet);
        walletBalanceRepository.save(destinationWallet);

        saveIdempotencyRecord(transferRequest, idempotencyKey);

        log.info("Transfer successful: {} -> {} Amount: {}",
                transferRequest.getSourceAccount(),
                transferRequest.getDestinationAccount(),
                transferRequest.getAmount());

        TransferRespDto response =
                WalletMapper.toTransferResponse("Transfer successful");

        return ResponseEntity.ok(response);
    }




    private void validateIdempotency(String key) {

        idempotencyRepository.findByIdempotencyKey(key)
                .ifPresent(existing -> {

                    if (existing.getStatus() == IdempotencyStatus.SUCCESS) {

                        log.warn("Duplicate transaction detected for key {}", key);

                        throw new DuplicateException("Duplicate transaction request");
                    }

                });
    }

    private Account getAccount(String accountNumber) {

        return accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountNumber));
    }

    private WalletBalance getWallet(Account account) {

        return account.getWalletBalance();
    }

    private void validateAccountStatus(Account account) {

        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InactiveAccountException("Account is not active");
        }
    }

    private void validateBalance(BigDecimal balance, BigDecimal amount) {

        if (balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
    }

    private void performTransfer(WalletBalance source, WalletBalance destination, BigDecimal amount) {

        source.setBalance(source.getBalance().subtract(amount));

        destination.setBalance(destination.getBalance().add(amount));
    }

    private void saveIdempotencyRecord(DoTransDto dto, String key) {

        IdempotencyKey record = new IdempotencyKey();

        record.setIdempotencyKey(key);
        record.setRequestHash(generateRequestHash(dto));
        record.setResponseBody("Transfer successful");
        record.setStatus(IdempotencyStatus.SUCCESS);

        idempotencyRepository.save(record);
    }

    private String generateRequestHash(DoTransDto transDto) {

        return transDto.getSourceAccount()
                + transDto.getDestinationAccount()
                + transDto.getAmount();
    }



    @Transactional
    @Override
    public ResponseEntity<TransferRespDto> fundAccount(@Valid FundAccountRequestDto fundAccountRequestDto) {

        log.info("Funding account with amount: {}", fundAccountRequestDto);

        Account account = getAccount(fundAccountRequestDto.getAccountNumber());
        if (account == null){
            throw new ResourceNotFoundException("Account not found: " + fundAccountRequestDto.getAccountNumber());
        }


        validateAccountStatus(account);
        WalletBalance wallet = getWallet(account);
        if (wallet == null){
            throw new ResourceNotFoundException("Wallet not found for account : " + fundAccountRequestDto.getAccountNumber());
        }

        wallet.setBalance(wallet.getBalance().add(fundAccountRequestDto.getAmount()));

        walletBalanceRepository.save(wallet);
        publisher.publishEvent(new FundAccountCreatedEvent(wallet));

        log.info("Account {} funded successfully. New balance: {}", fundAccountRequestDto.getAccountNumber(), wallet.getBalance());


        TransferRespDto response =
                WalletMapper.toTransferResponse("Account funded successfully. Current balance: " + wallet.getBalance());


        return ResponseEntity.ok(response);
    }


}
