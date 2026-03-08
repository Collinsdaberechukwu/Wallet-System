package com.collins.Wallet.System.event;

import com.collins.Wallet.System.model.Account;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccountCreatedEventListener implements ApplicationListener<@NonNull AccountCreatedEvent> {
    @Override
    public void onApplicationEvent(AccountCreatedEvent event) {
        log.info("Event Received: {}", event);
        Account account = (Account) event.getSource();
        String sms = "Welcome onboard, Your new account number is: "+ account.getAccountNumber();
        log.info("SMS Sent from mock notification: {}", sms);
    }

    @Override
    public boolean supportsAsyncExecution() {
        return true;
    }
}
