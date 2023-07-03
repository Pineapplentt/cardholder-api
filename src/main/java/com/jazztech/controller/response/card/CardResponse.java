package com.jazztech.controller.response.card;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CardResponse(
        String cardId,
        String cardNumber,
        Integer cvv,
        LocalDate dueDate,
        BigDecimal limit,
        LocalDate createdAt
) {
}
