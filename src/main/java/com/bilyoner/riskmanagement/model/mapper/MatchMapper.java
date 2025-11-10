package com.bilyoner.riskmanagement.model.mapper;

import com.bilyoner.riskmanagement.domain.entity.Match;
import com.bilyoner.riskmanagement.model.dto.response.MatchResponseDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper( componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MatchMapper {

    MatchResponseDto toResponseDto(Match match);

    List<MatchResponseDto> toResponseDtoList(List<Match> matches);
}
