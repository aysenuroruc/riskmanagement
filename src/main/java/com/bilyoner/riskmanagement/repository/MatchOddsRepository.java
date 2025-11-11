package com.bilyoner.riskmanagement.repository;

import com.bilyoner.riskmanagement.domain.MatchResult;
import com.bilyoner.riskmanagement.domain.entity.MatchOdds;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchOddsRepository extends JpaRepository<MatchOdds, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<MatchOdds> findAllByMatchId(Long matchId);

    Optional<MatchOdds> findByMatchIdAndResultType(Long matchId, MatchResult resultType);

}
