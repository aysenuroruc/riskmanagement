package com.bilyoner.riskmanagement.service.impl;

import com.bilyoner.riskmanagement.domain.MatchResult;
import com.bilyoner.riskmanagement.domain.entity.Match;
import com.bilyoner.riskmanagement.domain.entity.MatchOdds;
import com.bilyoner.riskmanagement.exception.InsufficientLimitException;
import com.bilyoner.riskmanagement.exception.MatchNotFoundException;
import com.bilyoner.riskmanagement.repository.MatchOddsRepository;
import com.bilyoner.riskmanagement.service.RiskManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Slf4j
@Service
@RequiredArgsConstructor
public class RiskManagementServiceImpl implements RiskManagementService {

    private final MatchOddsRepository matchOddsRepository;

    @Override
    public void validateRiskLimit(Long matchId, MatchResult selectedResult, BigDecimal betAmount) {
        MatchOdds odds = matchOddsRepository.findByMatchIdAndResultType(matchId, selectedResult)
                .orElseThrow(() -> new MatchNotFoundException("Match odds not found for result: " + selectedResult));

        BigDecimal availableLimit = odds.getAvailableLimit();

        if (betAmount.compareTo(availableLimit) > 0) {
            log.error("Bet amount {} exceeds available limit {} for match {}, result {}",
                    betAmount, availableLimit, matchId, selectedResult);

            Match match = odds.getMatch();

            throw new InsufficientLimitException(
                    match.getMatchName(),
                    selectedResult.getCode(),
                    availableLimit,
                    betAmount
            );
        }
    }
}
