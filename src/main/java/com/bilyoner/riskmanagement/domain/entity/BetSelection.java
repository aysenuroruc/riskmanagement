package com.bilyoner.riskmanagement.domain.entity;

import com.bilyoner.riskmanagement.domain.MatchResult;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bet_selections")
public class BetSelection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bet_id", nullable = false)
    private Bet bet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Enumerated(EnumType.STRING)
    @Column(name = "selected_result", nullable = false, length = 3)
    private MatchResult selectedResult;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal oddsAtBetTime;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public String getMatchName() {
        return match != null ? match.getMatchName() : "Unknown Match";
    }

    public String getFormattedSelection() {
        return String.format("%s - %s @ %.2f",
                getMatchName(),
                selectedResult.getCode(),
                oddsAtBetTime);
    }
}
