package com.bilyoner.riskmanagement.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BetSelectionResponseDTO {
    Long id;
    Long matchId;
    String homeTeam;
    String awayTeam;
    String selectedResult;
    BigDecimal oddsAtBetTime;
}
