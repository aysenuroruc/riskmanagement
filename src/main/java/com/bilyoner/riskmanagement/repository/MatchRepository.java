package com.bilyoner.riskmanagement.repository;

import com.bilyoner.riskmanagement.domain.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
}
