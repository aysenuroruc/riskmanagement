package com.bilyoner.riskmanagement.service;

import com.bilyoner.riskmanagement.model.domain.BetDO;

public interface BetService {
    BetDO placeBet(BetDO betRequest);
    void evictMatchesListCache();
}
