package com.bilyoner.riskmanagement.service.impl;

import com.bilyoner.riskmanagement.domain.entity.Match;
import com.bilyoner.riskmanagement.model.dto.response.MatchResponseDto;
import com.bilyoner.riskmanagement.model.mapper.MatchMapper;
import com.bilyoner.riskmanagement.repository.MatchRepository;
import com.bilyoner.riskmanagement.service.MatchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final MatchMapper matchMapper;

    @Override
    public List<MatchResponseDto> getAllMatches() {
        List<Match> matches = matchRepository.findAll();
        return matchMapper.toResponseDtoList(matches);
    }
}
