package com.bilyoner.riskmanagement.repository;

import com.bilyoner.riskmanagement.domain.entity.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {
    @Query("SELECT b FROM Bet b LEFT JOIN FETCH b.selections WHERE b.id = :id")
    Optional<Bet> findByIdWithSelections(@Param("id") Long id);
}
