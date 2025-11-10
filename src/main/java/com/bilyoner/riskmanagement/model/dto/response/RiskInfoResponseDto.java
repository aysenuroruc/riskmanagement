package com.bilyoner.riskmanagement.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
@Builder
public class RiskInfoResponseDto {
    private Long matchId;
    private String matchName;
    private Map<String, RiskDetailDto> riskByResult;

    @Data
    @Builder
    public static class RiskDetailDto {
        private BigDecimal currentRisk;
        private BigDecimal riskLimit;
        private BigDecimal availableLimit;
        private BigDecimal utilizationPercentage;
        private BigDecimal currentOdds;

    }
}
