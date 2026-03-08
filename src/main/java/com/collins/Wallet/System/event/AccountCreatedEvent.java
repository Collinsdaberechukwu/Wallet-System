package com.collins.Wallet.System.event;

import com.collins.Wallet.System.model.Account;
import org.springframework.context.ApplicationEvent;

public class AccountCreatedEvent extends ApplicationEvent {
    public AccountCreatedEvent(Account source) {
        super(source);
    }
}
