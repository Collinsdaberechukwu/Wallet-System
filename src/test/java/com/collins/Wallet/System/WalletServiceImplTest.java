package com.collins.Wallet.System;

import com.collins.Wallet.System.dtos.ResponseDto;
import com.collins.Wallet.System.dtos.ResponseDtos.TransferRespDto;
import com.collins.Wallet.System.dtos.ResquestDto.CreateUserRequestDto;
import com.collins.Wallet.System.dtos.ResquestDto.DoTransDto;
import com.collins.Wallet.System.enums.AccountStatus;
import com.collins.Wallet.System.enums.IdempotencyStatus;
import com.collins.Wallet.System.event.UserCreatedEvent;
import com.collins.Wallet.System.exception.DuplicateException;
import com.collins.Wallet.System.model.Account;
import com.collins.Wallet.System.model.IdempotencyKey;
import com.collins.Wallet.System.model.Users;
import com.collins.Wallet.System.model.WalletBalance;
import com.collins.Wallet.System.repository.AccountRepository;
import com.collins.Wallet.System.repository.IdempotencyRepository;
import com.collins.Wallet.System.repository.UserRepository;
import com.collins.Wallet.System.repository.WalletBalanceRepository;
import com.collins.Wallet.System.service.Impl.WalletServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WalletServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private WalletBalanceRepository walletBalanceRepository;

    @Mock
    private IdempotencyRepository idempotencyRepository;

    @Mock
    private ApplicationEventPublisher publisher;

    @InjectMocks
    private WalletServiceImpl walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_shouldCreateUserAndPublishEvent() {
        CreateUserRequestDto request = new CreateUserRequestDto();
        request.setEmail("collinsdaberechi20@gmail.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(Users.class))).thenAnswer(i -> i.getArguments()[0]);

        ResponseEntity<ResponseDto> response = walletService.createUser(request);

        assertNotNull(response.getBody());
        assertEquals("201", response.getBody().getStatusCode());
        assertEquals("User created successfully with wallet account", response.getBody().getStatusMessage());
        verify(publisher, times(1)).publishEvent(any(UserCreatedEvent.class));
    }

    @Test
    void transfer_shouldMoveFundsBetweenAccounts() {
        String sourceAccNum = "5020915812";
        String destAccNum = "5213775022";

        Account source = new Account();
        source.setAccountStatus(AccountStatus.ACTIVE);
        WalletBalance sourceWallet = new WalletBalance();
        sourceWallet.setBalance(new BigDecimal("1000"));
        source.setWalletBalance(sourceWallet);

        Account dest = new Account();
        dest.setAccountStatus(AccountStatus.ACTIVE);
        WalletBalance destWallet = new WalletBalance();
        destWallet.setBalance(new BigDecimal("500"));
        dest.setWalletBalance(destWallet);

        DoTransDto dto = new DoTransDto();
        dto.setSourceAccount(sourceAccNum);
        dto.setDestinationAccount(destAccNum);
        dto.setAmount(new BigDecimal("200"));

        when(accountRepository.findByAccountNumber(sourceAccNum)).thenReturn(Optional.of(source));
        when(accountRepository.findByAccountNumber(destAccNum)).thenReturn(Optional.of(dest));
        when(idempotencyRepository.findByIdempotencyKey(anyString())).thenReturn(Optional.empty());

        ResponseEntity<TransferRespDto> response = walletService.transfer(dto, "key1");

        assertEquals("Transfer successful", response.getBody().getMessage());
        assertEquals(new BigDecimal("800"), sourceWallet.getBalance());
        assertEquals(new BigDecimal("700"), destWallet.getBalance());

        verify(walletBalanceRepository).save(sourceWallet);
        verify(walletBalanceRepository).save(destWallet);
        verify(idempotencyRepository).save(any(IdempotencyKey.class));
    }

    @Test
    void fundAccount_shouldAddBalance() {
        String accountNumber = "5213775022";
        Account account = new Account();
        account.setAccountStatus(AccountStatus.ACTIVE);
        WalletBalance wallet = new WalletBalance();
        wallet.setBalance(new BigDecimal("100"));
        account.setWalletBalance(wallet);

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        ResponseEntity<TransferRespDto> response = walletService.fundAccount(accountNumber, new BigDecimal("200"));

        assertEquals("Account funded successfully. Current balance: 300", response.getBody().getMessage());
        assertEquals(new BigDecimal("300"), wallet.getBalance());
        verify(walletBalanceRepository).save(wallet);
    }

    @Test
    void fundAccount_shouldThrowExceptionForNegativeAmount() {
        assertThrows(IllegalArgumentException.class, () ->
                walletService.fundAccount("5020915812", BigDecimal.ZERO));
    }

    @Test
    void transfer_shouldThrowExceptionForDuplicateIdempotency() {
        IdempotencyKey key = new IdempotencyKey();
        key.setStatus(IdempotencyStatus.SUCCESS);
        when(idempotencyRepository.findByIdempotencyKey("dupKey")).thenReturn(Optional.of(key));

        DoTransDto dto = new DoTransDto();
        dto.setSourceAccount("5020915812");
        dto.setDestinationAccount("5213775022");
        dto.setAmount(BigDecimal.ONE);

        assertThrows(DuplicateException.class, () ->
                walletService.transfer(dto, "dupKey"));
    }
}

