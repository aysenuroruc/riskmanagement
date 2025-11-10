package com.bilyoner.riskmanagement.service;

import com.bilyoner.riskmanagement.domain.MatchResult;

import java.math.BigDecimal;

public interface RiskManagementService {
    void validateRiskLimit(Long matchId, MatchResult selectedResult, BigDecimal betAmount);
}
