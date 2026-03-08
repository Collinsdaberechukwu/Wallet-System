package com.collins.Wallet.System.service.Impl;

import com.collins.Wallet.System.enums.AccountStatus;
import com.collins.Wallet.System.event.AccountCreatedEvent;
import com.collins.Wallet.System.model.Account;
import com.collins.Wallet.System.model.Users;
import com.collins.Wallet.System.model.WalletBalance;
import com.collins.Wallet.System.repository.AccountRepository;
import com.collins.Wallet.System.service.AccountNumberGenerator;
import com.collins.Wallet.System.service.AccountService;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    private final ApplicationEventPublisher publisher;

    private final AccountNumberGenerator accountNumberGenerator;

    @Override
    public void createUserAccount(Users users) {
        Account account = new Account();
        account.setAccountNumber(accountNumberGenerator.generateAccountNumber());
        account.setAccountStatus(AccountStatus.ACTIVE);

        WalletBalance walletBalance = new WalletBalance();
        walletBalance.setBalance(BigDecimal.ZERO);

        account.setWalletBalance(walletBalance);
        account.setUser(users);

        account = accountRepository.save(account);

        publisher.publishEvent(new AccountCreatedEvent(account));
    }
}
