package com.bilyoner.riskmanagement.service;

import com.bilyoner.riskmanagement.model.dto.response.MatchResponseDto;

import java.util.List;

public interface MatchService {
    List<MatchResponseDto> getAllMatches();
    MatchResponseDto getMatchById(Long matchId);
}
