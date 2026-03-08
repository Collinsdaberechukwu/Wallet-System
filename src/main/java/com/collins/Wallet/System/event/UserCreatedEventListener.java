package com.collins.Wallet.System.event;

import com.collins.Wallet.System.model.Users;
import com.collins.Wallet.System.service.AccountService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserCreatedEventListener implements ApplicationListener<@NonNull UserCreatedEvent> {

    private final AccountService accountService;

    @Override
    public void onApplicationEvent(UserCreatedEvent event) {
        log.info("Event Received: {}", event);
        Users user = (Users) event.getSource();
        accountService.createUserAccount(user);

    }

    @Override
    public boolean supportsAsyncExecution() {
        return true;
    }
}
