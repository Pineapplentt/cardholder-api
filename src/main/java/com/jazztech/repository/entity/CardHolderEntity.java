package com.jazztech.repository.entity;

import com.jazztech.utils.CardHolderStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "CARD_HOLDER")
@Getter
@Immutable
@NoArgsConstructor
public class CardHolderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "card_holder_id")
    private UUID id;

    @Column(name = "client_id")
    private UUID clientId;

    @Column(name = "credit_analysis_id")
    private UUID creditAnalysisId;

    @JoinColumn(name = "bank_account_fk_id")
    @OneToOne(cascade = CascadeType.ALL)
    @Nullable
    private BankAccount bankAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_holder_status")
    private CardHolderStatus status;

    @Column(name = "card_holder_limit")
    private BigDecimal limit;

    @Column(name = "card_holder_available_limit")
    private BigDecimal availableLimit;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder(toBuilder = true)
    public CardHolderEntity(
            UUID id, UUID clientId, UUID creditAnalysisId, BankAccount bankAccount,
            CardHolderStatus status, BigDecimal limit, BigDecimal availableLimit, LocalDateTime createdAt) {
        this.id = id;
        this.clientId = clientId;
        this.creditAnalysisId = creditAnalysisId;
        this.bankAccount = bankAccount;
        this.status = status;
        this.limit = limit;
        this.availableLimit = availableLimit;
        this.createdAt = createdAt;
    }
}
