package com.bilyoner.riskmanagement.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
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
    @Builder.Default
    private List<MatchOdds> odds = new ArrayList<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
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
