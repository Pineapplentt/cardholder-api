package com.jazztech.controller.response.card;

import java.math.BigDecimal;

public record LimitUpdateResponse(
        String cardId,
        BigDecimal updatedLimit
) {
}
