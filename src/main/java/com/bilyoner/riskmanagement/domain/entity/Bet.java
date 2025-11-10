package com.bilyoner.riskmanagement.domain.entity;

import com.bilyoner.riskmanagement.domain.enums.BetStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Bet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal betAmount;

    @Column(nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal totalOdds = BigDecimal.ONE;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal potentialWin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private BetStatus status = BetStatus.PENDING;

    @OneToMany(mappedBy = "bet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<BetSelection> selections = new ArrayList<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
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
