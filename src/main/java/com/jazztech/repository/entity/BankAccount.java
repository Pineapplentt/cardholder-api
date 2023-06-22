package com.jazztech.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "BANK_ACCOUNT")
@Getter
@NoArgsConstructor
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "bank_account_id")
    private UUID bankAccountId;

    @Column(name = "account_number")
    private String account;

    @Column(name = "agency_number")
    private String agency;

    @Column(name = "bank_code")
    private String bankCode;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder(toBuilder = true)
    public BankAccount(UUID bankAccountId, String account, String agency, String bankCode) {
        this.bankAccountId = bankAccountId;
        this.account = account;
        this.agency = agency;
        this.bankCode = bankCode;
    }
}
