package com.bilyoner.riskmanagement.repository;

import com.bilyoner.riskmanagement.model.entity.Bet;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {
    @EntityGraph(attributePaths = "selections")
    Optional<Bet> findById(Long id);
}
