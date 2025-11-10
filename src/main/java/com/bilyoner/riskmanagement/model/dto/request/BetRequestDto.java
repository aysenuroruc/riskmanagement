package com.bilyoner.riskmanagement.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class BetRequestDto {
    @NotNull
    @DecimalMin(value = "0.01", message = "Bet amount must be at least 0.01")
    private BigDecimal betAmount;

    @NotEmpty
    @Valid
    private List<BetSelectionDto> selections;
}
