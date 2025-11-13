package com.bilyoner.riskmanagement.service.impl;

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
import com.bilyoner.riskmanagement.service.BetService;
import com.bilyoner.riskmanagement.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BetServiceImpl implements BetService {

    private final MatchService matchService;
    private final BetRepository betRepository;
    private final MatchOddServiceImpl matchOddService;
    private final BetMapper betMapper;

    private void validateBetRequest(BetDO betDO) {
        if (betDO.getSelections() == null || betDO.getSelections().isEmpty()) {
            throw new InvalidBetException("At least one selection is required");
        }

        if (betDO.getBetAmount() == null || betDO.getBetAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidBetException("Bet amount must be greater than zero");
        }
    }

    private void validateNoDuplicateMatches(List<BetSelectionDO> selections) {

        Map<Long, List<BetSelectionDO>> selectionsByMatchId =
                selections.stream()
                        .collect(Collectors.groupingBy(b -> b.getMatchId()));

        for (Long matchId: selectionsByMatchId.keySet()) {
            List<BetSelectionDO> betSelectionDOList = selectionsByMatchId.get(matchId);
            boolean hasDuplicateResult = betSelectionDOList.stream()
                    .map(BetSelectionDO::getSelectedResult)
                    .collect(Collectors.toSet())
                    .size() < betSelectionDOList.size();

            if (hasDuplicateResult) {
                throw new InvalidBetException("You can't bet to same match result more than one");
            }
        }
    }

    private void validateRiskLimitExceeded(BetDO betDO, BigDecimal payout) {
        for (BetSelectionDO betSelectionDO: betDO.getSelections()) {
            MatchOddsDO matchOddsDO = matchOddService.findAllMatchOddsByMatchIdAndResult(betSelectionDO.getMatchId(), betSelectionDO.getSelectedResult());

            BigDecimal calculatedRisk = matchOddsDO.getCurrentRisk().add(payout);

            if (calculatedRisk.compareTo(matchOddsDO.getRiskLimit()) >= 0) {
                throw new InvalidBetException("Risk limits are exceeded");
            }
        }
    }

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

        BigDecimal payout = calculatePayout(betDO);
        validateRiskLimitExceeded(betDO, payout);

        for (BetSelectionDO betSelectionDO: betDO.getSelections()) {
            List<MatchOddsDO> matchOddsDOList = matchOddService.findAllMatchOddsByMatchId(betSelectionDO.getMatchId());

            BigDecimal newTotalRisk = calculateNewTotalRisk(betSelectionDO, matchOddsDOList, payout);

            BigDecimal bookSum = calculateBookSum(matchOddsDOList);

            for (MatchOddsDO matchOddsDO: matchOddsDOList) {
                if (matchOddsDO.getResultType().equals(betSelectionDO.getSelectedResult())) {
                    betSelectionDO.setOddsAtBetTime(matchOddsDO.getOddsValue());
                    betSelectionDO.setMatch(findMatch(betSelectionDO.getMatchId()));
                    BigDecimal currentRisk = matchOddsDO.getCurrentRisk().add(payout);

                    matchOddsDO.setOddsValue(calculateNewOddsValue(currentRisk, newTotalRisk, bookSum));
                }
                else {
                    BigDecimal currentRisk = matchOddsDO.getCurrentRisk();
                    matchOddsDO.setOddsValue(calculateNewOddsValue(currentRisk, newTotalRisk, bookSum));
                }
                matchOddService.updateMatchOdds(matchOddsDO);
            }
        }

        Bet bet = betMapper.toEntity(betDO);
        bet.setCreatedAt(LocalDateTime.now());
        bet.setUpdatedAt(LocalDateTime.now());
        bet.setStatus(BetStatus.PENDING);
        for (BetSelection betSelection: bet.getSelections()) {
            betSelection.setBet(bet);
            betSelection.setCreatedAt(LocalDateTime.now());
        }
        bet = betRepository.save(bet);
        return betMapper.toDO(bet);
    }

    private BigDecimal calculateNewOddsValue(BigDecimal currentRisk, BigDecimal newTotalRisk, BigDecimal bookSum) {
        BigDecimal riskPay = currentRisk.divide(newTotalRisk, RoundingMode.HALF_UP);
        BigDecimal payoutRatio = new BigDecimal("1.0000").divide(bookSum, RoundingMode.HALF_UP);
        return payoutRatio.divide(riskPay, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateBookSum(List<MatchOddsDO> matchOddsDOList) {
        return matchOddsDOList.stream()
                .map(m->new BigDecimal("1.00").divide(m.getOddsValue(), RoundingMode.HALF_UP))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculatePayout(BetDO betDO) {
        BigDecimal comboOdds = betDO.getSelections()
                .stream().map(b -> {
                    MatchOddsDO matchOddsDO = matchOddService.findAllMatchOddsByMatchIdAndResult(b.getMatchId(), b.getSelectedResult());
                    return matchOddsDO.getOddsValue();
                })
                .reduce(BigDecimal.ONE, BigDecimal::multiply);

        return betDO.getBetAmount().multiply(comboOdds);
    }

    private BigDecimal calculateNewTotalRisk(BetSelectionDO betSelectionDO, List<MatchOddsDO> matchOddsDOList, BigDecimal payout) {
        BigDecimal newTotalRisk = matchOddsDOList.stream().map(m->m.getCurrentRisk())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return newTotalRisk.add(payout);
    }

    private MatchDO findMatch(long matchId) {
        MatchDO match = matchService.getMatchById(matchId);
        if (match == null) {
            throw new MatchNotFoundException("Match not found");
        }
        return match;
    }
}
