package com.collins.Wallet.System.model;

import com.collins.Wallet.System.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "accounts")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Account extends BaseEntity{

    @Column(nullable = false,unique = true)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccountStatus accountStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private WalletBalance walletBalance;
}
