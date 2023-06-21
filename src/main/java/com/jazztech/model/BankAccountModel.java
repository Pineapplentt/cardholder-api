package com.jazztech.model;

import java.util.UUID;
import lombok.Builder;

public record BankAccountModel(
        UUID bankAccountId,
        String account,
        String agency,
        String bankCode
) {
    @Builder(toBuilder = true)
    public BankAccountModel {
    }
}
