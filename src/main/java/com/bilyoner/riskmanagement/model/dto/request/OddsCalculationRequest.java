package com.bilyoner.riskmanagement.model.dto.request;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@Builder
public class OddsCalculationRequest {
    private final BigDecimal currentOdds;
    private final BigDecimal currentRisk;
    private final BigDecimal additionalRisk;
    private final BigDecimal riskLimit;
    private final boolean selectedOutcome;

    public static OddsCalculationRequest from(com.bilyoner.riskmanagement.domain.entity.MatchOdds odds,
                                              BigDecimal additionalRisk, boolean selectedOutcome) {
        return OddsCalculationRequest.builder()
                .currentOdds(odds.getOddsValue())
                .currentRisk(odds.getCurrentRisk())
                .additionalRisk(additionalRisk)
                .riskLimit(odds.getRiskLimit())
                .selectedOutcome(selectedOutcome)
                .build();
    }
}
