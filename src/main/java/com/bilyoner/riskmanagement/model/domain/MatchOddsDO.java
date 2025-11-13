package com.bilyoner.riskmanagement.model.domain;

import com.bilyoner.riskmanagement.enums.MatchResult;
import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchOddsDO {
    long id;
    MatchResult resultType;
    BigDecimal oddsValue;
    BigDecimal currentRisk;
    BigDecimal riskLimit;
}
