package com.jazztech.controller.response.cardholder;

import com.jazztech.controller.response.CardHolderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CardHolderResponse(
        UUID cardHolderId,
        CardHolderStatus status,
        BigDecimal limit,
        LocalDateTime createdAt
) {
}
