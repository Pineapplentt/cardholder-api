package com.jazztech.controller.response.card;

import java.math.BigDecimal;
import lombok.Builder;

public record LimitUpdateResponse(
        String cardId,
        BigDecimal updatedLimit
) {
    @Builder(toBuilder = true)
    public LimitUpdateResponse(String cardId, BigDecimal updatedLimit) {
        this.cardId = cardId;
        this.updatedLimit = updatedLimit;
    }
}
