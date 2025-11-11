package com.bilyoner.riskmanagement.service;

import com.bilyoner.riskmanagement.domain.MatchResult;
import com.bilyoner.riskmanagement.domain.entity.MatchOdds;
import com.bilyoner.riskmanagement.model.dto.request.OddsCalculationRequest;

import java.math.BigDecimal;
import java.util.List;

public interface OddsCalculationService {
    void updateOddsAfterBet(Long matchId, MatchResult selectedResult, BigDecimal betAmount);

    BigDecimal calculateNewOdds(OddsCalculationRequest request);

    boolean validateNoGuaranteedWin(List<MatchOdds> allOdds);
}
