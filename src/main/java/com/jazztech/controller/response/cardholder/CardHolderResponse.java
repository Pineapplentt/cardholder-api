package com.jazztech.controller.response.cardholder;

import com.jazztech.utils.CardHolderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CardHolderResponse(
        UUID id,
        CardHolderStatus status,
        BigDecimal limit,
        LocalDateTime createdAt
) {
}
