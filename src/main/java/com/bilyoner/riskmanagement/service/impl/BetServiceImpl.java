package com.bilyoner.riskmanagement.service.impl;

import com.bilyoner.riskmanagement.domain.MatchResult;
import com.bilyoner.riskmanagement.domain.builder.BetBuilder;
import com.bilyoner.riskmanagement.domain.entity.Bet;
import com.bilyoner.riskmanagement.domain.entity.Match;
import com.bilyoner.riskmanagement.domain.entity.MatchOdds;
import com.bilyoner.riskmanagement.exception.InvalidBetException;
import com.bilyoner.riskmanagement.exception.MatchNotFoundException;
import com.bilyoner.riskmanagement.model.dto.request.BetRequestDto;
import com.bilyoner.riskmanagement.model.dto.request.BetSelectionDto;
import com.bilyoner.riskmanagement.model.dto.response.BetResponseDto;
import com.bilyoner.riskmanagement.model.mapper.BetMapper;
import com.bilyoner.riskmanagement.repository.BetRepository;
import com.bilyoner.riskmanagement.repository.MatchOddsRepository;
import com.bilyoner.riskmanagement.service.BetService;
import com.bilyoner.riskmanagement.service.MatchService;
import com.bilyoner.riskmanagement.service.OddsCalculationService;
import com.bilyoner.riskmanagement.service.RiskManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BetServiceImpl implements BetService {

    private final MatchService matchService;
    private final BetBuilder betBuilder;
    private final BetRepository betRepository;
    private final BetMapper betMapper;
    private final MatchOddsRepository matchOddsRepository;
    private final OddsCalculationService oddsCalculationService;
    private final RiskManagementService riskManagementService;

    @Override
    @Transactional
    public BetResponseDto placeBet(BetRequestDto betRequest) {

        validateBetRequest(betRequest);

        validateNoDuplicateMatches(betRequest.getSelections());

        List<BetBuilder.SelectionData> selectionDataList = new ArrayList<>();

        for (BetSelectionDto selectionDto : betRequest.getSelections()) {
            Long matchId = selectionDto.getMatchId();
            MatchResult selectedResult = MatchResult.fromCode(selectionDto.getSelectedResult());

            Match match = matchService.findMatchEntityById(matchId);

            MatchOdds currentOdds = matchOddsRepository.findByMatchIdAndResultType(matchId, selectedResult)
                    .orElseThrow(() -> new MatchNotFoundException("Odds not found for match: " + matchId));

            riskManagementService.validateRiskLimit(matchId, selectedResult, betRequest.getBetAmount());

            selectionDataList.add(new BetBuilder.SelectionData(
                    match,
                    selectedResult,
                    currentOdds.getOddsValue()
            ));
        }

        Bet bet = betBuilder.buildBet(betRequest.getBetAmount(), selectionDataList);
        Bet savedBet = betRepository.save(bet);

        for (BetSelectionDto selectionDto : betRequest.getSelections()) {
            MatchResult selectedResult = MatchResult.fromCode(selectionDto.getSelectedResult());
            oddsCalculationService.updateOddsAfterBet(
                    selectionDto.getMatchId(),
                    selectedResult,
                    betRequest.getBetAmount()
            );
        }
        return betMapper.toResponseDto(savedBet);
    }

    @Override
    @Transactional(readOnly = true)
    public BetResponseDto getBetById(Long betId) {
        Bet bet = betRepository.findByIdWithSelections(betId)
                .orElseThrow(() -> {
                    log.error("Bet not found with id: {}", betId);
                    return new InvalidBetException("Bet not found with id: " + betId);
                });
        return betMapper.toResponseDto(bet);
    }

    private void validateBetRequest(BetRequestDto betRequest) {
        if (betRequest.getBetAmount() == null || betRequest.getBetAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidBetException("Bet amount must be greater than zero");
        }

        if (betRequest.getSelections() == null || betRequest.getSelections().isEmpty()) {
            throw new InvalidBetException("At least one selection is required");
        }
    }

    private void validateNoDuplicateMatches(List<BetSelectionDto> selections) {
        Set<Long> matchIds = new HashSet<>();
        for (BetSelectionDto selection : selections) {
            if (!matchIds.add(selection.getMatchId())) {
                throw new InvalidBetException(
                        "Duplicate match selection not allowed. Match ID: " + selection.getMatchId()
                );
            }
        }
    }
}
