package com.jazztech.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;

public record CardModel(
        UUID cardHolderId,
        UUID cardId,
        BigDecimal limit,
        String cardNumber,
        Integer cvv,
        LocalDate dueDate
) {
    @Builder(toBuilder = true)
    public CardModel {
    }

    public CardModel updateCardLimit(BigDecimal newLimit) {
        return this.toBuilder().limit(newLimit).build();
    }
}
