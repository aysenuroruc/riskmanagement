package com.bilyoner.riskmanagement.service.impl;

import com.bilyoner.riskmanagement.domain.entity.Match;
import com.bilyoner.riskmanagement.domain.entity.MatchOdds;
import com.bilyoner.riskmanagement.exception.MatchNotFoundException;
import com.bilyoner.riskmanagement.model.dto.response.MatchResponseDto;
import com.bilyoner.riskmanagement.model.dto.response.RiskInfoResponseDto;
import com.bilyoner.riskmanagement.model.mapper.MatchMapper;
import com.bilyoner.riskmanagement.repository.MatchRepository;
import com.bilyoner.riskmanagement.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;

    @Override
    public List<MatchResponseDto> getAllMatches() {
        List<Match> matches = matchRepository.findAll();
        return matchMapper.toResponseDtoList(matches);
    }

    @Override
    public MatchResponseDto getMatchById(Long matchId) {
        Match match = findMatchEntityById(matchId);
        return matchMapper.toResponseDto(match);
    }

    @Override
    public Match findMatchEntityById(Long matchId) {
        return matchRepository.findByIdWithOdds(matchId)
                .orElseThrow(() -> {
                    log.error("Match not found: {}", matchId);
                    return new MatchNotFoundException(matchId);
                });
    }

    @Override
    public RiskInfoResponseDto getMatchRiskInfo(Long matchId) {
        Match match = findMatchEntityById(matchId);

        Map<String, RiskInfoResponseDto.RiskDetailDto> riskByResult = new HashMap<>();

        for (MatchOdds odds : match.getOdds()) {
            RiskInfoResponseDto.RiskDetailDto detail = RiskInfoResponseDto.RiskDetailDto.builder()
                    .currentRisk(odds.getCurrentRisk())
                    .riskLimit(odds.getRiskLimit())
                    .availableLimit(odds.getAvailableLimit())
                    .utilizationPercentage(odds.getRiskUtilization())
                    .currentOdds(odds.getOddsValue())
                    .build();
            riskByResult.put(odds.getResultType().getCode(), detail);
        }

        return RiskInfoResponseDto.builder()
                .matchId(match.getId())
                .matchName(match.getMatchName())
                .riskByResult(riskByResult)
                .build();
    }
}
