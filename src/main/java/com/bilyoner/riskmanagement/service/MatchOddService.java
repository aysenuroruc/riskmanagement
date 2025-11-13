package com.bilyoner.riskmanagement.service;

import com.bilyoner.riskmanagement.enums.MatchResult;
import com.bilyoner.riskmanagement.model.domain.MatchOddsDO;

import java.util.List;

public interface MatchOddService {
    void updateMatchOdds(MatchOddsDO matchOddsDO);

    List<MatchOddsDO> findAllMatchOddsByMatchId(long matchId);

    MatchOddsDO findAllMatchOddsByMatchIdAndResult(long matchId, MatchResult matchResult);
}
