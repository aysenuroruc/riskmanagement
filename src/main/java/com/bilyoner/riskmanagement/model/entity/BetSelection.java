package com.bilyoner.riskmanagement.model.entity;

import com.bilyoner.riskmanagement.enums.MatchResult;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bet_selection")
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
}
