package com.bilyoner.riskmanagement.model.mapper;

import com.bilyoner.riskmanagement.model.domain.MatchOddsDO;
import com.bilyoner.riskmanagement.model.entity.MatchOdds;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MatchOddMapper {
    MatchOddsDO toDO(MatchOdds match);
    MatchOdds toEntity(MatchOddsDO matchOddsDO);
}
