package com.bilyoner.riskmanagement.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "match")
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    Team homeTeam;

    @ManyToOne
    Team awayTeam;

    @Column(nullable = false)
    LocalDateTime matchDate;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<MatchOdds> odds = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    LocalDateTime createdAt;

    @Column(nullable = false)
    LocalDateTime updatedAt;
}
