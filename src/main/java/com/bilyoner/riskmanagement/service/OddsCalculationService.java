package com.bilyoner.riskmanagement.service;

import com.bilyoner.riskmanagement.domain.MatchResult;
import com.bilyoner.riskmanagement.domain.entity.MatchOdds;

import java.math.BigDecimal;
import java.util.List;

public interface OddsCalculationService {
    void updateOddsAfterBet(Long matchId, MatchResult selectedResult, BigDecimal betAmount);

    BigDecimal calculateNewOdds(
            BigDecimal currentOdds,
            BigDecimal currentRisk,
            BigDecimal additionalRisk,
            BigDecimal riskLimit,
            boolean isSelectedOutcome
    );

    boolean validateNoGuaranteedWin(List<MatchOdds> allOdds);
}
