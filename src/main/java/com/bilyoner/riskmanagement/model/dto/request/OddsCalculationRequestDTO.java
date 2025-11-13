package com.bilyoner.riskmanagement.model.dto.request;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@Builder
public class OddsCalculationRequestDTO {
    private final BigDecimal currentOdds;
    private final BigDecimal currentRisk;
    private final BigDecimal additionalRisk;
    private final BigDecimal riskLimit;
    private final boolean selectedOutcome;
}
