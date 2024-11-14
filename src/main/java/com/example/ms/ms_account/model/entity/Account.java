package com.example.ms.ms_account.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Account extends  BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="account_id")
    private Long accountId;

    @Column(name="account_number")
    private String accountNumber;

    @Column(name="balance")
    private Double balance;

    @Column(name="account_type")
    private String accountType;

    @Column(name="customer_id")
    private String customerId;
}