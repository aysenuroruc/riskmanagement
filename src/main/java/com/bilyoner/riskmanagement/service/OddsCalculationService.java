package com.bilyoner.riskmanagement.service;

import com.bilyoner.riskmanagement.model.domain.BetDO;
import com.bilyoner.riskmanagement.model.domain.MatchOddsDO;
import java.math.BigDecimal;
import java.util.List;

public interface OddsCalculationService {
    BigDecimal calculateNewOddsValue(BigDecimal currentRisk, BigDecimal newTotalRisk, BigDecimal bookSum);
    BigDecimal calculateBookSum(List<MatchOddsDO> matchOddsDOList);
    BigDecimal calculatePayout(BetDO betDO);
    BigDecimal calculateNewTotalRisk(List<MatchOddsDO> matchOddsDOList, BigDecimal payout);
}

