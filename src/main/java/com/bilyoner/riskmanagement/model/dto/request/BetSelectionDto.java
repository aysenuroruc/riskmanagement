package com.bilyoner.riskmanagement.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BetSelectionDto {

    @NotNull
    private Long matchId;

    @NotNull
    @Schema(required = true, allowableValues = {"1", "X", "2"})
    private String selectedResult;
}
