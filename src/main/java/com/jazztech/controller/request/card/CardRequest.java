package com.jazztech.controller.request.card;

import java.math.BigDecimal;

public record CardRequest(
        String cardHolderId,
        BigDecimal limit
) {
}
