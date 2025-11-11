package com.bilyoner.riskmanagement.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "matches")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String homeTeam;

    @Column(nullable = false, length = 100)
    private String awayTeam;

    @Column(nullable = false)
    private LocalDateTime matchDate;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<MatchOdds> odds = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public String getMatchName() {
        return homeTeam + " - " + awayTeam;
    }

    public void addOdds(MatchOdds matchOdds) {
        odds.add(matchOdds);
        matchOdds.setMatch(this);
    }

    public void removeOdds(MatchOdds matchOdds) {
        odds.remove(matchOdds);
        matchOdds.setMatch(null);
    }
}
