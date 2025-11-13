package com.bilyoner.riskmanagement.model.dto.request;

import com.bilyoner.riskmanagement.enums.MatchResult;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BetSelectionDTO {
    @NotNull
    long matchId;

    @NotNull
    MatchResult selectedResult;
}
