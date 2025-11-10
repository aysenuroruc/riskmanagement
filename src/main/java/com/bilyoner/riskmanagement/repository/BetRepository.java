package com.bilyoner.riskmanagement.repository;

import com.bilyoner.riskmanagement.domain.entity.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BetRepository extends JpaRepository<Bet, Long> {

}
