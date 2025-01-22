package com.jazztech.controller.response.card;

import java.math.BigDecimal;
import java.util.UUID;

public record LimitUpdateResponse(
        UUID cardId,
        BigDecimal updatedLimit
) {
}
