package com.bilyoner.riskmanagement.service.impl;

import com.bilyoner.riskmanagement.model.domain.MatchDO;
import com.bilyoner.riskmanagement.model.dto.response.MatchResponseDTO;
import com.bilyoner.riskmanagement.model.entity.Match;
import com.bilyoner.riskmanagement.model.mapper.MatchMapper;
import com.bilyoner.riskmanagement.repository.MatchRepository;
import com.bilyoner.riskmanagement.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    MatchMapper matchMapper = Mappers.getMapper(MatchMapper.class);


    @Override
    public List<MatchResponseDTO> getAllMatches() {
        List<Match> matches = matchRepository.findAll();
        return matchMapper.toResponseDtoList(matches);
    }

    @Override
    public MatchDO getMatchById(Long id) {
        return matchMapper.toDO(matchRepository.findById(id).orElse(null));
    }
}