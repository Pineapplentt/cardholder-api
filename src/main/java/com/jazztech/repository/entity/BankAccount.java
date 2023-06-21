package com.jazztech.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.util.UUID;

@Entity
public class BankAccount {
    @Id
    @Column(name = "bank_account_id")
    private UUID bankAccountId;

    @Column(name = "account_number")
    private String account;

    @Column(name = "agency_number")
    private String agency;

    @Column(name = "bank_code")
    private String bankCode;
}
