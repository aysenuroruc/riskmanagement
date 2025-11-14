package com.bilyoner.riskmanagement.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class CacheEvictService {
    @CacheEvict(value = "matches-list", allEntries = true)
    public void evictMatchesList() {
        // this method for just evicting cache for all entries in "matches-list"
    }
}
