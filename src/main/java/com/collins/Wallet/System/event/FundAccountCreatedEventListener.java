package com.collins.Wallet.System.event;

import com.collins.Wallet.System.model.WalletBalance;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FundAccountCreatedEventListener implements ApplicationListener<@NonNull FundAccountCreatedEvent> {



    @Override
    public void onApplicationEvent(@NonNull FundAccountCreatedEvent event) {

        WalletBalance walletBalances = (WalletBalance) event.getSource();

        log.info("Funds account event received. Account funded successfully. Current account balance:  {}",
                walletBalances.getBalance());
    }

    @Override
    public boolean supportsAsyncExecution() {
        return true;
    }
}
