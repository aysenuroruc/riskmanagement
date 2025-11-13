package com.bilyoner.riskmanagement.model.entity;


import com.bilyoner.riskmanagement.enums.MatchResult;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "match_odd", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"match_id", "result_type"})
})
public class MatchOdds {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "match_id", nullable = false)
    Match match;

    @Enumerated(EnumType.STRING)
    @Column(name = "result_type", nullable = false, length = 3)
    MatchResult resultType;

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal oddsValue;

    @Column(nullable = false, precision = 15, scale = 2)
    BigDecimal currentRisk = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    BigDecimal riskLimit;

    @Column(nullable = false)
    LocalDateTime updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;
}
