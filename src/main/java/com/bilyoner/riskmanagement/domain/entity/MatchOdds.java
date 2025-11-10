package com.bilyoner.riskmanagement.domain.entity;

import com.bilyoner.riskmanagement.domain.MatchResult;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "match_odds", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"match_id", "result_type"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class MatchOdds {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Enumerated(EnumType.STRING)

    @Column(name = "result_type", nullable = false, length = 3)

    private MatchResult resultType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal oddsValue;

    @Column(nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal currentRisk = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal riskLimit;

    @LastModifiedDate
    @Column(nullable = false)

    private LocalDateTime updatedAt;

    public BigDecimal getAvailableLimit() {

        return riskLimit.subtract(currentRisk);

    }

    public BigDecimal getRiskUtilization() {
        if (riskLimit.compareTo(BigDecimal.ZERO) == 0) {

            return BigDecimal.ZERO;

        }
        return currentRisk.divide(riskLimit, 4, BigDecimal.ROUND_HALF_UP);

    }

    public void addRisk(BigDecimal amount) {
        this.currentRisk = this.currentRisk.add(amount);
    }

    public void updateOddsValue(BigDecimal newOddsValue, BigDecimal minOddsValue) {
        this.oddsValue = newOddsValue.compareTo(minOddsValue) < 0 ? minOddsValue : newOddsValue;
    }
}
