package com.jazztech.cardholder;

import com.jazztech.cardholder.dto.AnalysisSearch;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "creditAnalysisApi", url = "http://localhost:9001/credit/analysis")
public interface CreditAnalysisClient {
    @GetMapping(path = "/{id}")
    AnalysisSearch getAllAnalysis(@PathVariable UUID id);
}
