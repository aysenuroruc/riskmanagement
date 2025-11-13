package com.bilyoner.riskmanagement.model.domain;

import com.bilyoner.riskmanagement.enums.MatchResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetSelectionDO {
    long matchId;
    MatchDO match;
    MatchResult selectedResult;
    BigDecimal oddsAtBetTime;
}
