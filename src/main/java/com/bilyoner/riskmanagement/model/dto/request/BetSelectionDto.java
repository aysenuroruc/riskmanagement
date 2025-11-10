package com.bilyoner.riskmanagement.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BetSelectionDto {

    @NotNull
    private Long matchId;

    @NotNull
    private String selectedResult;  //allowable values = {"1", "X", "2"}
}
