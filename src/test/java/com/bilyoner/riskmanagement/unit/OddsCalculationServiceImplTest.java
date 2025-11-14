package com.bilyoner.riskmanagement.unit;

import com.bilyoner.riskmanagement.constants.BetConstants;
import com.bilyoner.riskmanagement.enums.MatchResult;
import com.bilyoner.riskmanagement.model.domain.BetDO;
import com.bilyoner.riskmanagement.model.domain.BetSelectionDO;
import com.bilyoner.riskmanagement.model.domain.MatchOddsDO;
import com.bilyoner.riskmanagement.service.impl.MatchOddServiceImpl;
import com.bilyoner.riskmanagement.service.impl.OddsCalculationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OddsCalculationServiceImplTest {
    private final MatchOddServiceImpl matchOddService = mock(MatchOddServiceImpl.class);
    private final OddsCalculationServiceImpl oddsCalculationService = new OddsCalculationServiceImpl(matchOddService);

    @Test
    void calculateBookSum_shouldSumInverseOdds() {
        MatchOddsDO odds1 = new MatchOddsDO();
        odds1.setOddsValue(new BigDecimal("2.0"));
        MatchOddsDO odds2 = new MatchOddsDO();
        odds2.setOddsValue(new BigDecimal("4.0"));

        List<MatchOddsDO> oddsList = List.of(odds1, odds2);

        BigDecimal result = oddsCalculationService.calculateBookSum(oddsList);

        BigDecimal expected = BetConstants.ONE.divide(new BigDecimal("2.0"), BigDecimal.ROUND_HALF_UP)
                .add(BetConstants.ONE.divide(new BigDecimal("4.0"), BigDecimal.ROUND_HALF_UP));
        assertThat(result).isEqualByComparingTo(expected);
    }

    @Test
    void calculatePayout_shouldMultiplyOddsAndBetAmount() {
        BetSelectionDO sel1 = new BetSelectionDO();
        sel1.setMatchId(1L);
        sel1.setSelectedResult(MatchResult.MS1);

        BetSelectionDO sel2 = new BetSelectionDO();
        sel2.setMatchId(2L);
        sel2.setSelectedResult(MatchResult.MSX);

        BetDO betDO = new BetDO();
        betDO.setBetAmount(new BigDecimal("10"));
        betDO.setSelections(List.of(sel1, sel2));

        MatchOddsDO odds1 = new MatchOddsDO();
        odds1.setOddsValue(new BigDecimal("2.0"));
        MatchOddsDO odds2 = new MatchOddsDO();
        odds2.setOddsValue(new BigDecimal("3.0"));

        when(matchOddService.findAllMatchOddsByMatchIdAndResult(1L, MatchResult.MS1)).thenReturn(odds1);
        when(matchOddService.findAllMatchOddsByMatchIdAndResult(2L, MatchResult.MSX)).thenReturn(odds2);

        BigDecimal result = oddsCalculationService.calculatePayout(betDO);

        assertThat(result).isEqualByComparingTo(new BigDecimal("60.0"));
    }

    @Test
    void calculateNewTotalRisk_shouldSumCurrentRisksAndAddPayout() {
        MatchOddsDO odds1 = new MatchOddsDO();
        odds1.setCurrentRisk(new BigDecimal("5"));
        MatchOddsDO odds2 = new MatchOddsDO();
        odds2.setCurrentRisk(new BigDecimal("7"));

        List<MatchOddsDO> oddsList = List.of(odds1, odds2);
        BigDecimal payout = new BigDecimal("3");

        BigDecimal result = oddsCalculationService.calculateNewTotalRisk(oddsList, payout);

        assertThat(result).isEqualByComparingTo(new BigDecimal("15"));
    }
}
