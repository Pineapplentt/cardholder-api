package com.jazztech.cardholder.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;

public record AnalysisSearch(
        UUID analysisId,
        UUID clientId,
        BigDecimal approvedLimit
) {
    @Builder(toBuilder = true)
    public AnalysisSearch {
    }
}
