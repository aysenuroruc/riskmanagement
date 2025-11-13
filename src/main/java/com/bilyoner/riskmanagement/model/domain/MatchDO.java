package com.bilyoner.riskmanagement.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchDO {
    Long id;
    TeamDO homeTeam;
    TeamDO awayTeam;
    LocalDateTime matchDate;
    List<MatchOddsDO> odds;
}
