package com.collins.Wallet.System.model;

import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "wallet_balances")
@Entity
public class WalletBalance extends BaseEntity {

    @Column(nullable = false)
    private BigDecimal balance;

    @OneToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
