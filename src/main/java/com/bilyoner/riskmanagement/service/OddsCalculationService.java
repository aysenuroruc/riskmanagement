package com.bilyoner.riskmanagement.service;

import com.bilyoner.riskmanagement.model.entity.MatchOdds;
import com.bilyoner.riskmanagement.model.dto.request.OddsCalculationRequestDTO;

import java.math.BigDecimal;
import java.util.List;

public interface OddsCalculationService {
    BigDecimal calculateNewOdds(OddsCalculationRequestDTO request);

    boolean validateNoGuaranteedWin(List<MatchOdds> allOdds);
}
