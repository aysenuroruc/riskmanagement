package com.bilyoner.riskmanagement.model.mapper;

import com.bilyoner.riskmanagement.domain.entity.Match;
import com.bilyoner.riskmanagement.domain.entity.MatchOdds;
import com.bilyoner.riskmanagement.model.dto.response.MatchResponseDto;
import com.bilyoner.riskmanagement.model.dto.response.RiskInfoResponseDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper( componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MatchMapper {
    MatchMapper INSTANCE = Mappers.getMapper(MatchMapper.class);

    MatchResponseDto toResponseDto(Match match);

    List<MatchResponseDto> toResponseDtoList(List<Match> matches);

    @Mapping(target = "currentRisk", source = "currentRisk")
    @Mapping(target = "riskLimit", source = "riskLimit")
    @Mapping(target = "availableLimit", source = "availableLimit")
    @Mapping(target = "utilizationPercentage", source = "riskUtilization")
    @Mapping(target = "currentOdds", source = "oddsValue")
    RiskInfoResponseDto.RiskDetailDto toRiskDetailDto(MatchOdds odds);
}
