package com.bilyoner.riskmanagement.model.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MatchOddsResponseDto {

    private String resultType; // ex: MS1
    private BigDecimal riskLimit;
    private BigDecimal oddsValue;
    private BigDecimal currentRisk;
    private BigDecimal availableLimit;
    private BigDecimal riskUtilization; // ex: 0.80
}
