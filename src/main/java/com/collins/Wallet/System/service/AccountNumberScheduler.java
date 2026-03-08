package com.collins.Wallet.System.service;

import com.collins.Wallet.System.enums.AccountStatus;
import com.collins.Wallet.System.model.Account;
import com.collins.Wallet.System.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountNumberScheduler {

    private final AccountRepository accountRepository;
    private final AccountNumberGenerator accountNumberGenerator;

    @Scheduled(cron = "0 0 * * * *")
    public void regenerateAccountNumbers() {

        List<Account> accounts =
                accountRepository.findByAccountStatusAndAccountNumberIsNull(AccountStatus.ACTIVE);

        for (Account account : accounts) {

            String accountNumber = accountNumberGenerator.generateAccountNumber();

            account.setAccountNumber(accountNumber);

            accountRepository.save(account);

            log.info("Generated account number {} for account id {}", accountNumber, account.getId());
        }
    }
}
