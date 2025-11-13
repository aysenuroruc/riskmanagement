package com.bilyoner.riskmanagement.repository;

import com.bilyoner.riskmanagement.model.entity.Match;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    @EntityGraph(attributePaths = "odds")
    Optional<Match> findById(Long id);
}
