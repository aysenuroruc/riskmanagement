package com.bilyoner.riskmanagement.model.entity;

import com.bilyoner.riskmanagement.enums.BetStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bet")
public class Bet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal betAmount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private BetStatus status = BetStatus.PENDING;

    @OneToMany(mappedBy = "bet", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<BetSelection> selections = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
