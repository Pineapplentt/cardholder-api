package com.jazztech.model;

import com.jazztech.repository.entity.CardHolderEntity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;

public record CardModel(
        CardHolderEntity cardHolder,
        UUID cardId,
        BigDecimal limit,
        String cardNumber,
        Integer cvv,
        LocalDate dueDate
) {
    @Builder(toBuilder = true)
    public CardModel {
    }
}
