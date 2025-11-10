package com.bilyoner.riskmanagement.domain.builder;

import com.bilyoner.riskmanagement.domain.MatchResult;
import com.bilyoner.riskmanagement.domain.entity.Bet;
import com.bilyoner.riskmanagement.domain.entity.BetSelection;
import com.bilyoner.riskmanagement.domain.entity.Match;
import com.bilyoner.riskmanagement.domain.enums.BetStatus;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class BetBuilder {
    public record SelectionData(Match match, MatchResult selectedResult, BigDecimal oddsValue) {
    }

    public Bet buildBet(BigDecimal betAmount, List<SelectionData> selections) {
        Bet bet = Bet.builder()
                .betAmount(betAmount)
                .status(BetStatus.PENDING)
                .build();

        for (SelectionData selectionData : selections) {

            BetSelection selection = BetSelection.builder()
                    .bet(bet)
                    .match(selectionData.match())
                    .selectedResult(selectionData.selectedResult())
                    .oddsAtBetTime(selectionData.oddsValue())
                    .build();
            bet.addSelection(selection);
        }

        // Calculate total odds and potential win
        bet.calculateTotalOdds();
        bet.calculatePotentialWin();
        return bet;
    }

    public Bet buildSingleBet(
            BigDecimal betAmount,
            Match match,
            MatchResult selectedResult,
            BigDecimal oddsValue) {
        SelectionData selectionData = new SelectionData(match, selectedResult, oddsValue);
        return buildBet(betAmount, List.of(selectionData));

    }
}
