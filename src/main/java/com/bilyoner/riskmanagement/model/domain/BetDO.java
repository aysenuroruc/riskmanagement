package com.bilyoner.riskmanagement.model.domain;

import com.bilyoner.riskmanagement.enums.BetStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetDO {
    Long id;
    BigDecimal betAmount;
    BetStatus betStatus;
    List<BetSelectionDO> selections = new ArrayList<>();
}
