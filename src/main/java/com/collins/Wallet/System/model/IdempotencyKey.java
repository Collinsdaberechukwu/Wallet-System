package com.collins.Wallet.System.model;


import com.collins.Wallet.System.enums.IdempotencyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Getter
@Setter
@Table(name = "idempotency_keys")
@AllArgsConstructor
@NoArgsConstructor
public class IdempotencyKey extends BaseEntity {


    @Column(unique = true,nullable = false)
    private String idempotencyKey;

    @Column(nullable = false)
    private String requestHash;

    @Lob
    @Column(nullable = false)
    private String responseBody;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IdempotencyStatus status;

}
