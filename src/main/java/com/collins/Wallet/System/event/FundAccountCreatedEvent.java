package com.collins.Wallet.System.event;

import com.collins.Wallet.System.model.WalletBalance;
import org.springframework.context.ApplicationEvent;

public class FundAccountCreatedEvent extends ApplicationEvent {

    public FundAccountCreatedEvent(WalletBalance walletBalance) {
        super(walletBalance);
    }
}
