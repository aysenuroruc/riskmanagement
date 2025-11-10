package com.bilyoner.riskmanagement.service;

import com.bilyoner.riskmanagement.domain.entity.Match;
import com.bilyoner.riskmanagement.model.dto.response.MatchResponseDto;
import com.bilyoner.riskmanagement.model.dto.response.RiskInfoResponseDto;

import java.util.List;

public interface MatchService {

    List<MatchResponseDto> getAllMatches();

    MatchResponseDto getMatchById(Long id);

    Match findMatchEntityById(Long matchId);

    RiskInfoResponseDto getMatchRiskInfo(Long matchId);

}
