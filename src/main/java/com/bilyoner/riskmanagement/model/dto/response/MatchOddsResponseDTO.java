package com.bilyoner.riskmanagement.model.dto.response;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MatchOddsResponseDTO {
    String resultType;
    BigDecimal oddsValue;
}
