package com.jazztech.model;

import com.jazztech.repository.entity.BankAccount;
import com.jazztech.utils.CardHolderStatus;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

public record CardHolderModel(
        UUID cardHolderId,
        UUID creditAnalysisId,
        BankAccount bankAccount,
        CardHolderStatus status,
        BigDecimal limit
) {
    @Builder(toBuilder = true)
    public CardHolderModel {
    }
}
