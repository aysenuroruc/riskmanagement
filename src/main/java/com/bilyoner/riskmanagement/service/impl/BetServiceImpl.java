package com.bilyoner.riskmanagement.service.impl;

import com.bilyoner.riskmanagement.constants.BetConstants;
import com.bilyoner.riskmanagement.enums.BetStatus;
import com.bilyoner.riskmanagement.exception.InvalidBetException;
import com.bilyoner.riskmanagement.exception.MatchNotFoundException;
import com.bilyoner.riskmanagement.model.domain.BetDO;
import com.bilyoner.riskmanagement.model.domain.BetSelectionDO;
import com.bilyoner.riskmanagement.model.domain.MatchDO;
import com.bilyoner.riskmanagement.model.domain.MatchOddsDO;
import com.bilyoner.riskmanagement.model.entity.Bet;
import com.bilyoner.riskmanagement.model.entity.BetSelection;
import com.bilyoner.riskmanagement.model.mapper.BetMapper;
import com.bilyoner.riskmanagement.repository.BetRepository;
import com.bilyoner.riskmanagement.service.*;
import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BetServiceImpl implements BetService {
    private final BetMapper betMapper;
    private final Counter betPlacedCounter;
    private final MatchService matchService;
    private final BetRepository betRepository;
    private final MatchOddService matchOddService;
    private final CacheEvictService cacheEvictService;
    private final OddsCalculationService oddsCalculationService;

    @Retryable(
            value = {
                    org.springframework.dao.PessimisticLockingFailureException.class,
                    org.springframework.dao.CannotAcquireLockException.class,
                    org.springframework.dao.ConcurrencyFailureException.class
            },
            maxAttempts = 3,
            backoff = @Backoff(delay = 50)
    )
    @Transactional
    @Override
    public BetDO placeBet(BetDO betDO) {
        validateBetRequest(betDO);
        validateNoDuplicateMatches(betDO.getSelections());
        BigDecimal payout = oddsCalculationService.calculatePayout(betDO);
        validateRiskLimitExceeded(betDO, payout);
        updateOddsAndSelections(betDO, payout);
        Bet bet = createBetEntity(betDO);
        bet = saveBet(bet);
        betPlacedCounter.increment();
        return betMapper.toDO(bet);
    }

    private void updateOddsAndSelections(BetDO betDO, BigDecimal payout) {
        for (BetSelectionDO betSelectionDO : betDO.getSelections()) {
            List<MatchOddsDO> matchOddsDOList = matchOddService.findAllMatchOddsByMatchId(betSelectionDO.getMatchId());
            BigDecimal newTotalRisk = oddsCalculationService.calculateNewTotalRisk(matchOddsDOList, payout);
            BigDecimal bookSum = oddsCalculationService.calculateBookSum(matchOddsDOList);
            for (MatchOddsDO matchOddsDO : matchOddsDOList) {
                BigDecimal currentRisk = matchOddsDO.getCurrentRisk();
                if (matchOddsDO.getResultType().equals(betSelectionDO.getSelectedResult())) {
                    betSelectionDO.setOddsAtBetTime(matchOddsDO.getOddsValue());
                    betSelectionDO.setMatch(findMatch(betSelectionDO.getMatchId()));
                    currentRisk = currentRisk.add(payout);
                }
                matchOddsDO.setOddsValue(oddsCalculationService.calculateNewOddsValue(currentRisk, newTotalRisk, bookSum));
                matchOddService.updateMatchOdds(matchOddsDO);
            }
        }
        cacheEvictService.evictMatchesList();
    }

    private Bet createBetEntity(BetDO betDO) {
        Bet bet = betMapper.toEntity(betDO);
        bet.setCreatedAt(LocalDateTime.now());
        bet.setUpdatedAt(LocalDateTime.now());
        bet.setStatus(BetStatus.PENDING);
        for (BetSelection betSelection : bet.getSelections()) {
            betSelection.setBet(bet);
            betSelection.setCreatedAt(LocalDateTime.now());
        }
        return bet;
    }

    private Bet saveBet(Bet bet) {
        return betRepository.save(bet);
    }

    private MatchDO findMatch(long matchId) {
        MatchDO match = matchService.getMatchById(matchId);
        if (match == null) {
            throw new MatchNotFoundException(BetConstants.ERR_MATCH_NOT_FOUND);
        }
        return match;
    }

    private void validateBetRequest(BetDO betDO) {
        if (betDO.getSelections() == null || betDO.getSelections().isEmpty()) {
            throw new InvalidBetException(BetConstants.ERR_SELECTION_REQUIRED);
        }

        if (betDO.getBetAmount() == null || betDO.getBetAmount().compareTo(BetConstants.ZERO) <= 0) {
            throw new InvalidBetException(BetConstants.ERR_BET_AMOUNT);
        }
    }

    private void validateNoDuplicateMatches(List<BetSelectionDO> selections) {
        Map<Long, List<BetSelectionDO>> selectionsByMatchId =
                selections.stream()
                        .collect(Collectors.groupingBy(BetSelectionDO::getMatchId));
        for (Long matchId: selectionsByMatchId.keySet()) {
            List<BetSelectionDO> betSelectionDOList = selectionsByMatchId.get(matchId);
            boolean hasDuplicateResult = betSelectionDOList.stream()
                    .map(BetSelectionDO::getSelectedResult)
                    .collect(Collectors.toSet())
                    .size() < betSelectionDOList.size();
            if (hasDuplicateResult) {
                throw new InvalidBetException(BetConstants.ERR_DUPLICATE_RESULT);
            }
        }
    }

    private void validateRiskLimitExceeded(BetDO betDO, BigDecimal payout) {
        for (BetSelectionDO betSelectionDO: betDO.getSelections()) {
            MatchOddsDO matchOddsDO = matchOddService.findAllMatchOddsByMatchIdAndResult(betSelectionDO.getMatchId(),
                    betSelectionDO.getSelectedResult());

            BigDecimal calculatedRisk = matchOddsDO.getCurrentRisk().add(payout);

            if (calculatedRisk.compareTo(matchOddsDO.getRiskLimit()) >= 0) {
                throw new InvalidBetException(BetConstants.ERR_RISK_LIMIT);
            }
        }
    }
}
