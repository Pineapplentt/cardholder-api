package com.jazztech.controller.response.card;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record CardResponse(
        UUID cardId,
        String cardNumber,
        Integer cvv,
        LocalDate dueDate,
        BigDecimal limit,
        LocalDateTime createdAt
) {
}
