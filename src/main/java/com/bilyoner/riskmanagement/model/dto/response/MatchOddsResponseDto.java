package com.bilyoner.riskmanagement.model.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MatchOddsResponseDto {

    @Schema(example = "MS1")
    private String resultType;

    private BigDecimal oddsValue;

    private BigDecimal currentRisk;

    private BigDecimal riskLimit;

    private BigDecimal availableLimit;

    @Schema(example = "0.80")
    private BigDecimal riskUtilization;
}
