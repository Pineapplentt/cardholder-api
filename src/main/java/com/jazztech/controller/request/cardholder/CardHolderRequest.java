package com.jazztech.controller.request.cardholder;

import com.jazztech.repository.entity.BankAccount;
import lombok.Builder;

@Builder
public record CardHolderRequest(
        String clientId,
        String creditAnalysisId,
        BankAccount bankAccount
) {
}
