package com.collins.Wallet.System.model;

import jakarta.persistence.*;
import lombok.*;


import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "users")
@Entity
public class Users extends BaseEntity {

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false,unique = true)
    private String email;

}
