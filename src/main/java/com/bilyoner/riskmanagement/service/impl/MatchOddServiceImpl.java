package com.bilyoner.riskmanagement.service.impl;

import com.bilyoner.riskmanagement.enums.MatchResult;
import com.bilyoner.riskmanagement.model.domain.MatchOddsDO;
import com.bilyoner.riskmanagement.model.entity.MatchOdds;
import com.bilyoner.riskmanagement.model.mapper.MatchOddMapper;
import com.bilyoner.riskmanagement.repository.MatchOddRepository;
import com.bilyoner.riskmanagement.service.MatchOddService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MatchOddServiceImpl implements MatchOddService {
    private final MatchOddRepository matchOddRepository;
    private final MatchOddMapper matchOddMapper;

    @Override
    public void updateMatchOdds(MatchOddsDO matchOddsDO) {
        MatchOdds matchOdds = matchOddRepository.findById(matchOddsDO.getId())
                .orElseThrow(() -> new EntityNotFoundException("Match odd not found"));
        matchOdds.setRiskLimit(matchOddsDO.getRiskLimit());
        matchOdds.setOddsValue(matchOddsDO.getOddsValue());
        matchOddRepository.save(matchOdds);
    }

    @Override
    public List<MatchOddsDO> findAllMatchOddsByMatchId(long matchId) {
        return matchOddRepository.findAllByMatchId(matchId).stream()
                .map(matchOddMapper::toDO)
                .toList();
    }

    @Override
    public MatchOddsDO findAllMatchOddsByMatchIdAndResult(long matchId, MatchResult matchResult) {
        return matchOddMapper.toDO(matchOddRepository.findByMatchIdAndResultType(matchId, matchResult).get());
    }
}
