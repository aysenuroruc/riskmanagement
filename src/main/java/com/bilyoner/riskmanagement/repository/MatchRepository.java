package com.bilyoner.riskmanagement.repository;

import com.bilyoner.riskmanagement.domain.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    @Query("SELECT m FROM Match m LEFT JOIN FETCH m.odds WHERE m.id = :id")
    Optional<Match> findByIdWithOdds(Long id);
}
