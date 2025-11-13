package com.bilyoner.riskmanagement.service.impl;

import com.bilyoner.riskmanagement.enums.MatchResult;
import com.bilyoner.riskmanagement.repository.MatchOddRepository;
import com.bilyoner.riskmanagement.service.RiskManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class RiskManagementServiceImpl implements RiskManagementService {

    private final MatchOddRepository matchOddRepository;

    @Override
    public void validateRiskLimit(Long matchId, MatchResult selectedResult, BigDecimal betAmount) {

    }
}
