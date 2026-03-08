package com.collins.Wallet.System.event;

import com.collins.Wallet.System.model.Users;
import org.springframework.context.ApplicationEvent;

public class UserCreatedEvent extends ApplicationEvent {
    public UserCreatedEvent(Users user) {
        super(user);
    }
}
