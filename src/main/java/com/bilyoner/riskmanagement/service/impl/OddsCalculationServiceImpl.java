package com.bilyoner.riskmanagement.service.impl;

import com.bilyoner.riskmanagement.config.BettingConfigProperties;
import com.bilyoner.riskmanagement.domain.MatchResult;
import com.bilyoner.riskmanagement.service.OddsCalculationService;
import com.bilyoner.riskmanagement.domain.entity.MatchOdds;
import com.bilyoner.riskmanagement.exception.InvalidOddsException;
import com.bilyoner.riskmanagement.repository.MatchOddsRepository;
import com.bilyoner.riskmanagement.model.dto.request.OddsCalculationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OddsCalculationServiceImpl implements OddsCalculationService {
    private final MatchOddsRepository matchOddsRepository;
    private final BettingConfigProperties config;

    @Override
    @Transactional
    public void updateOddsAfterBet(Long matchId, MatchResult selectedResult, BigDecimal betAmount) {
        List<MatchOdds> allOdds = matchOddsRepository.findAllByMatchId(matchId);
        if (allOdds.size() != 3) {
            throw new InvalidOddsException("Match must have exactly 3 odds entries");
        }

        MatchOdds selectedOdds = allOdds.stream()
                .filter(odds -> odds.getResultType() == selectedResult)
                .findFirst()
                .orElseThrow(() -> new InvalidOddsException("Selected result odds not found"));

        OddsCalculationRequest request = OddsCalculationRequest.from(selectedOdds, betAmount, true);
        BigDecimal newSelectedOdds = calculateNewOdds(request);

        // Calculate adjustment amount for other odds
        BigDecimal oddsReduction = selectedOdds.getOddsValue().subtract(newSelectedOdds);
        BigDecimal increasePerOther = oddsReduction
                .multiply(config.getOdds().getIncreaseCoefficient())
                .divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);


        // Update selected odds
        selectedOdds.updateOddsValue(newSelectedOdds, config.getOdds().getMinValue());
        selectedOdds.addRisk(betAmount);

        // Update other odds (increase)
        for (MatchOdds odds : allOdds) {
            if (odds.getResultType() != selectedResult) {
                BigDecimal newOdds = odds.getOddsValue().add(increasePerOther);
                odds.setOddsValue(newOdds);
            }
        }

        if (!validateNoGuaranteedWin(allOdds)) {
            adjustOddsToPreventGuaranteedWin(allOdds);
        }
        matchOddsRepository.saveAll(allOdds);
    }

    @Override
    public BigDecimal calculateNewOdds(OddsCalculationRequest request) {
        BigDecimal newRisk = request.getCurrentRisk().add(request.getAdditionalRisk());
        BigDecimal riskPercentage = newRisk.divide(request.getRiskLimit(), 4, RoundingMode.HALF_UP);

        if (request.isSelectedOutcome()) {
            // Decrease odds for selected outcome
            BigDecimal reductionFactor = BigDecimal.ONE.subtract(
                    riskPercentage.multiply(config.getOdds().getReductionCoefficient())
            );
            BigDecimal newOdds = request.getCurrentOdds().multiply(reductionFactor).setScale(2, RoundingMode.HALF_UP);

            return newOdds.max(config.getOdds().getMinValue());
        } else {
            // Increase odds for other outcomes
            BigDecimal increaseFactor = BigDecimal.ONE.add(
                    riskPercentage.multiply(config.getOdds().getIncreaseCoefficient())
            );
            return request.getCurrentOdds().multiply(increaseFactor).setScale(2, RoundingMode.HALF_UP);
        }
    }

    @Override
    public boolean validateNoGuaranteedWin(List<MatchOdds> allOdds) {
        // Calculate sum of inverse odds (probability sum)
        // If sum >= 1, it creates a guaranteed win scenario
        BigDecimal probabilitySum = allOdds.stream()
                .map(odds -> BigDecimal.ONE.divide(odds.getOddsValue(), 4, RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Must be less than 1.0 to ensure house edge
        return probabilitySum.compareTo(BigDecimal.ONE.subtract(config.getRisk().getHouseEdgeMargin())) < 0;
    }

    private void adjustOddsToPreventGuaranteedWin(List<MatchOdds> allOdds) {
        BigDecimal probabilitySum = allOdds.stream()
                .map(odds -> BigDecimal.ONE.divide(odds.getOddsValue(), 4, RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal targetSum = BigDecimal.ONE.subtract(config.getRisk().getHouseEdgeMargin());
        BigDecimal adjustmentFactor = probabilitySum.divide(targetSum, 4, RoundingMode.HALF_UP);

        for (MatchOdds odds : allOdds) {
            BigDecimal adjustedOdds = odds.getOddsValue()
                    .multiply(adjustmentFactor)
                    .setScale(2, RoundingMode.HALF_UP);
            odds.setOddsValue(adjustedOdds.max(config.getOdds().getMinValue()));
        }
    }
}
