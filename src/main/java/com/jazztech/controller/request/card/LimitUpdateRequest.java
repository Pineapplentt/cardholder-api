package com.jazztech.controller.request.card;

import java.math.BigDecimal;
import java.util.UUID;

public record LimitUpdateRequest(
        UUID cardId,
        BigDecimal limit) {
}
