package com.bilyoner.riskmanagement.domain.entity;

import com.bilyoner.riskmanagement.domain.enums.BetStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bets")
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal betAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalOdds = BigDecimal.ONE;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal potentialWin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BetStatus status = BetStatus.PENDING;

    @OneToMany(mappedBy = "bet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<BetSelection> selections = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public void addSelection(BetSelection selection) {
        selections.add(selection);
        selection.setBet(this);
    }

    public void removeSelection(BetSelection selection) {
        selections.remove(selection);
        selection.setBet(null);
    }

    public void calculateTotalOdds() {
        this.totalOdds = selections.stream()
                .map(BetSelection::getOddsAtBetTime)
                .reduce(BigDecimal.ONE, BigDecimal::multiply);
    }

    public void calculatePotentialWin() {
        this.potentialWin = betAmount.multiply(totalOdds);
    }

    public boolean isSingleBet() {
        return selections.size() == 1;
    }

    public int getSelectionCount() {
        return selections.size();
    }
}
