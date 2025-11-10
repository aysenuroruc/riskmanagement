package com.bilyoner.riskmanagement.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BetSelectionResponseDto {

    private Long id;

    private Long matchId;

    @Schema(example = "Galatasaray - Göztepe")
    private String matchName;

    private String selectedResult;

    private BigDecimal oddsAtBetTime;
}
