package com.bilyoner.riskmanagement.model.mapper;

import com.bilyoner.riskmanagement.model.domain.BetDO;
import com.bilyoner.riskmanagement.model.domain.BetSelectionDO;
import com.bilyoner.riskmanagement.model.domain.MatchDO;
import com.bilyoner.riskmanagement.model.dto.request.BetRequestDTO;
import com.bilyoner.riskmanagement.model.dto.request.BetSelectionDTO;
import com.bilyoner.riskmanagement.model.dto.response.BetSelectionResponseDTO;
import com.bilyoner.riskmanagement.model.entity.Bet;
import com.bilyoner.riskmanagement.model.dto.response.BetResponseDTO;
import com.bilyoner.riskmanagement.model.entity.BetSelection;
import com.bilyoner.riskmanagement.model.entity.Match;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BetMapper {
    BetResponseDTO toResponseDTO(BetDO betDO);

    @Mapping(target = "homeTeam", source = "match.homeTeam.name")
    BetSelectionResponseDTO toResponseDto(BetSelection betSelection);

    BetDO toDO(Bet bet);

    BetSelectionDO toDO(BetSelection betSelection);

    BetDO toDO(BetRequestDTO betRequestDTO);
    BetSelectionDO toDO(BetSelectionDTO betSelectionDTO);

    Bet toEntity(BetDO betDO);
    BetSelection toEntity(BetSelectionDO betSelectionDO);
    Match toEntity(MatchDO matchDO);
}
