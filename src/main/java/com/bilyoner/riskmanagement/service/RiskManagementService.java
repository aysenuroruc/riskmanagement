package com.bilyoner.riskmanagement.service;

import com.bilyoner.riskmanagement.enums.MatchResult;

import java.math.BigDecimal;

public interface RiskManagementService {
    void validateRiskLimit(Long matchId, MatchResult selectedResult, BigDecimal betAmount);
}
