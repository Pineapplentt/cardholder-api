package com.jazztech.repository.entity;

import com.jazztech.controller.response.CardHolderStatus;
import com.jazztech.model.BankAccountModel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "CARD_HOLDER")
@Immutable
@Getter
@NoArgsConstructor
public class CardHolderEntity {
    @Id
    @Column(name = "card_holder_id")
    private UUID cardHolderId;

    @Column(name = "credit_analysis_id")
    private UUID creditAnalysisId;

    @Column(name = "bank_account_fk_id")
    private BankAccountModel bankAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_holder_status")
    private CardHolderStatus status;

    @Column(name = "card_holder_limit")
    private BigDecimal limit;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
}
