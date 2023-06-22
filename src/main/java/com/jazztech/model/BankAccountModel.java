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
    public BankAccountModel(UUID bankAccountId, String account, String agency, String bankCode) {
        this.bankAccountId = bankAccountId;
        this.account = account;
        this.agency = agency;
        this.bankCode = bankCode;
    }
}
