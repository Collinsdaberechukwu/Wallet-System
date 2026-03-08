package com.collins.Wallet.System.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AccountStatus {
    ACTIVE("Account is Active"),
    SUSPENDED("Account is suspended"),
    CLOSED("Account is closed");

    private final String accountStatus;
}
