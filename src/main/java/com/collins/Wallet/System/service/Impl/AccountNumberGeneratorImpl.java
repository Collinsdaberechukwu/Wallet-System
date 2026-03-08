package com.collins.Wallet.System.service.Impl;

import com.collins.Wallet.System.repository.AccountRepository;
import com.collins.Wallet.System.service.AccountNumberGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class AccountNumberGeneratorImpl implements AccountNumberGenerator {

    private final AccountRepository accountRepository;

    private static final SecureRandom RANDOM = new SecureRandom();

    private static final int ACCOUNT_LENGTH = 10;

    private static final char START_DIGIT = '5';

    @Override
    public String generateAccountNumber() {

        String accountNumber;

        do {
            accountNumber = generateRandomNumber();
        }
        while (accountRepository.existsByAccountNumber(accountNumber));

        return accountNumber;
    }

    private String generateRandomNumber() {

        StringBuilder accountNumber = new StringBuilder();

        accountNumber.append(START_DIGIT);

        for (int i = 1; i < ACCOUNT_LENGTH; i++) {
            accountNumber.append(RANDOM.nextInt(10));
        }

        return accountNumber.toString();
    }
}
