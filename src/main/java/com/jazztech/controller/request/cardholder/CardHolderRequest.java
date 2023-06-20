package com.jazztech.controller.request.cardholder;

import com.jazztech.model.BankAccountModel;

public record CardHolderRequest(
        String clientId,
        String creditAnalysisId,
        BankAccountModel bankAccountModel
) {
}
