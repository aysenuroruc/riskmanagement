package com.bilyoner.riskmanagement.service;

import com.bilyoner.riskmanagement.model.domain.MatchDO;
import com.bilyoner.riskmanagement.model.dto.response.MatchResponseDTO;

import java.util.List;

public interface MatchService {

    List<MatchResponseDTO> getAllMatches();

    MatchDO getMatchById(Long id);

}
