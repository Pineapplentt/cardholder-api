package com.jazztech.controller.request.card;

import java.math.BigDecimal;

public record LimitUpdateRequest(
        String cardId,
        BigDecimal limit) {
}
