package com.jazztech.model;

import com.jazztech.repository.entity.BankAccount;
import com.jazztech.utils.CardHolderStatus;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

public record CardHolderModel(
        UUID id,
        UUID clientId,
        UUID creditAnalysisId,
        BankAccount bankAccount,
        CardHolderStatus status,
        BigDecimal limit,
        BigDecimal availableLimit
) {
    @Builder(toBuilder = true)
    public CardHolderModel {
    }
}
