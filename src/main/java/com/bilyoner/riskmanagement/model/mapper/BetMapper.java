package com.bilyoner.riskmanagement.model.mapper;

import com.bilyoner.riskmanagement.domain.entity.Bet;
import com.bilyoner.riskmanagement.model.dto.response.BetResponseDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface BetMapper {
    BetResponseDto toResponseDto(Bet bet);
}
