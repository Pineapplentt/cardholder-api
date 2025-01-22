package com.jazztech.controller.request.cardholder;

import com.jazztech.repository.entity.BankAccount;
import java.util.UUID;
import lombok.Builder;

@Builder
public record CardHolderRequest(
        UUID clientId,
        UUID creditAnalysisId,
        BankAccount bankAccount
) {
}
