package com.jazztech.model;

import com.jazztech.controller.response.CardHolderStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record CardHolderModel(
        UUID cardHolderId,
        UUID creditAnalysisId,
        BankAccountModel bankAccount,
        CardHolderStatus status,
        BigDecimal limit
) {
}
