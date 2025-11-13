package com.bilyoner.riskmanagement.model.mapper;

import com.bilyoner.riskmanagement.model.domain.TeamDO;
import com.bilyoner.riskmanagement.model.entity.Team;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamMapper {
    TeamDO toDO(Team team);
}
