package com.jazztech.controller.response.card;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CardListResponse(
        UUID cardId,
        String cardNumber,
        Integer cvv,
        LocalDate dueDate,
        BigDecimal limit
) {
}
