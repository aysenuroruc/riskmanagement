package com.bilyoner.riskmanagement.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RiskInfoResponseDTO {
    Long matchId;
    String matchName;
    Map<String, RiskDetailDto> riskByResult;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RiskDetailDto {
        BigDecimal currentRisk;
        BigDecimal riskLimit;
        BigDecimal availableLimit;
        BigDecimal utilizationPercentage;
        BigDecimal currentOdds;

    }
}
