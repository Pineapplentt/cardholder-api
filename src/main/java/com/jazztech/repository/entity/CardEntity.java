package com.jazztech.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;

@Entity
@Immutable
@Table(name = "CARD")
@Getter
@NoArgsConstructor
public class CardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "card_id")
    private UUID cardId;

    @Column(name = "card_holder_id")
    private UUID cardHolderId;
    @Column(name = "card_limit")
    private BigDecimal limit;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "cvv")
    private Integer cvv;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder(toBuilder = true)
    public CardEntity(UUID cardId, UUID cardHolderId, BigDecimal limit, String cardNumber, Integer cvv, LocalDate dueDate, LocalDateTime createdAt) {
        this.cardId = cardId;
        this.cardHolderId = cardHolderId;
        this.limit = limit;
        this.cardNumber = cardNumber;
        this.cvv = cvv;
        this.dueDate = dueDate;
        this.createdAt = createdAt;
    }
}
