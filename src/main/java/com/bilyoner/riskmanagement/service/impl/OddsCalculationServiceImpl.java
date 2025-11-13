package com.bilyoner.riskmanagement.service.impl;

import com.bilyoner.riskmanagement.constants.BetConstants;
import com.bilyoner.riskmanagement.model.domain.BetDO;
import com.bilyoner.riskmanagement.model.domain.MatchOddsDO;
import com.bilyoner.riskmanagement.service.OddsCalculationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@AllArgsConstructor
public class OddsCalculationServiceImpl implements OddsCalculationService {
    private final MatchOddServiceImpl matchOddService;

    @Override
    public BigDecimal calculateNewOddsValue(BigDecimal currentRisk, BigDecimal newTotalRisk, BigDecimal bookSum) {
        BigDecimal riskPay = currentRisk.divide(newTotalRisk, RoundingMode.HALF_UP);
        BigDecimal payoutRatio = new BigDecimal(BetConstants.PAYOUT_RATIO).divide(bookSum, RoundingMode.HALF_UP);
        return payoutRatio.divide(riskPay, RoundingMode.HALF_UP);
    }

    @Override
    public BigDecimal calculateBookSum(List<MatchOddsDO> matchOddsDOList) {
        return matchOddsDOList.stream()
                .map(m -> BetConstants.ONE.divide(m.getOddsValue(), RoundingMode.HALF_UP))
                .reduce(BetConstants.ZERO, BigDecimal::add);
    }

    @Override
    public BigDecimal calculatePayout(BetDO betDO) {
        BigDecimal comboOdds = betDO.getSelections()
                .stream().map(b -> {
                    MatchOddsDO matchOddsDO = matchOddService.findAllMatchOddsByMatchIdAndResult(b.getMatchId(), b.getSelectedResult());
                    return matchOddsDO.getOddsValue();
                })
                .reduce(BetConstants.ONE, BigDecimal::multiply);
        return betDO.getBetAmount().multiply(comboOdds);
    }

    @Override
    public BigDecimal calculateNewTotalRisk(List<MatchOddsDO> matchOddsDOList, BigDecimal payout) {
        BigDecimal newTotalRisk = matchOddsDOList.stream().map(MatchOddsDO::getCurrentRisk)
                .reduce(BetConstants.ZERO, BigDecimal::add);
        return newTotalRisk.add(payout);
    }
}
