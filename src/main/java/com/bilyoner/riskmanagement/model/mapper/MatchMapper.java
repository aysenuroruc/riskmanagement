package com.bilyoner.riskmanagement.model.mapper;

import com.bilyoner.riskmanagement.model.domain.MatchDO;
import com.bilyoner.riskmanagement.model.entity.Match;
import com.bilyoner.riskmanagement.model.entity.MatchOdds;
import com.bilyoner.riskmanagement.model.dto.response.MatchResponseDTO;
import com.bilyoner.riskmanagement.model.dto.response.RiskInfoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MatchMapper {

    @Mapping(source = "homeTeam.name", target = "homeTeam")
    @Mapping(source = "awayTeam.name", target = "awayTeam")
    MatchResponseDTO toDTO(MatchDO match);


    MatchDO toDO(Match match);
    Match toEntity(MatchDO matchDO);

    List<MatchResponseDTO> toResponseDtoList(List<Match> matches);

    @Mapping(target = "currentOdds", source = "oddsValue")
    RiskInfoResponseDTO.RiskDetailDto toRiskDetailDto(MatchOdds odds);
}
