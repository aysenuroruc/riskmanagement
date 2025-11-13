package com.bilyoner.riskmanagement.repository;

import com.bilyoner.riskmanagement.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findByName(String name);
}
