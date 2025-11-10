package com.bilyoner.riskmanagement.service;

import com.bilyoner.riskmanagement.model.dto.request.BetRequestDto;
import com.bilyoner.riskmanagement.model.dto.response.BetResponseDto;

public interface BetService {
    BetResponseDto placeBet(BetRequestDto betRequest);

    BetResponseDto getBetById(Long betId);
}
