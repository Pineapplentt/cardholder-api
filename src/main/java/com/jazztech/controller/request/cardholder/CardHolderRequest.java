package com.jazztech.controller.request.cardholder;

import com.jazztech.repository.entity.BankAccount;

public record CardHolderRequest(
        String clientId,
        String creditAnalysisId,
        BankAccount bankAccount
) {
}
