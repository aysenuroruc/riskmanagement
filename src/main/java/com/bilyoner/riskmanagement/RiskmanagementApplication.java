package com.bilyoner.riskmanagement;

import com.bilyoner.riskmanagement.enums.MatchResult;
import com.bilyoner.riskmanagement.model.entity.Match;
import com.bilyoner.riskmanagement.model.entity.MatchOdds;
import com.bilyoner.riskmanagement.model.entity.Team;
import com.bilyoner.riskmanagement.repository.MatchOddRepository;
import com.bilyoner.riskmanagement.repository.MatchRepository;
import com.bilyoner.riskmanagement.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@EnableRetry
@SpringBootApplication
public class RiskmanagementApplication implements CommandLineRunner {


    @Autowired
    MatchRepository matchRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    MatchOddRepository matchOddRepository;

    public static void main(String[] args) {
        SpringApplication.run(RiskmanagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String[] teamNames = new String[] {"Galatasaray", "Goztepe", "Besiktas", "Ankaragucu", "Fenerbahce", "Samsunspor"};
        for (String teamName: teamNames) {
            Team team = Team.builder()
                    .name(teamName)
                    .build();

            teamRepository.save(team);
        }

        GSGZT();
        BSKANK();
        FBSMN();

    }

    private void GSGZT() {
        Match match = Match.builder()
                .homeTeam(teamRepository.findByName("Galatasaray"))
                .awayTeam(teamRepository.findByName("Goztepe"))
                .createdAt(LocalDateTime.now())
                .matchDate(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        match = matchRepository.save(match);

        BigDecimal[] oddValues1 = new BigDecimal[] {new BigDecimal("1.21"), new BigDecimal("100000.00"), new BigDecimal("80000.00")};
        BigDecimal[] oddValuesX = new BigDecimal[] {new BigDecimal("3.00"), new BigDecimal("50000.00"), new BigDecimal("20000.00")};
        BigDecimal[] oddValues2 = new BigDecimal[] {new BigDecimal("2.50"), new BigDecimal("80000.00"), new BigDecimal("50000.00")};

        addMatchOdds(match, oddValues1, oddValuesX, oddValues2);
    }

    private void BSKANK() {
        Match match = Match.builder()
                .homeTeam(teamRepository.findByName("Besiktas"))
                .awayTeam(teamRepository.findByName("Ankaragucu"))
                .createdAt(LocalDateTime.now())
                .matchDate(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        match = matchRepository.save(match);

        BigDecimal[] oddValues1 = new BigDecimal[] {new BigDecimal("1.60"), new BigDecimal("60000.00"), new BigDecimal("50000.00")};
        BigDecimal[] oddValuesX = new BigDecimal[] {new BigDecimal("2.20"), new BigDecimal("30000.00"), new BigDecimal("15000.00")};
        BigDecimal[] oddValues2 = new BigDecimal[] {new BigDecimal("3.12"), new BigDecimal("40000.00"), new BigDecimal("20000.00")};

        addMatchOdds(match, oddValues1, oddValuesX, oddValues2);
    }

    private void FBSMN() {
        Match match = Match.builder()
                .homeTeam(teamRepository.findByName("Fenerbahce"))
                .awayTeam(teamRepository.findByName("Samsunspor"))
                .createdAt(LocalDateTime.now())
                .matchDate(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        match = matchRepository.save(match);

        BigDecimal[] oddValues1 = new BigDecimal[] {new BigDecimal("1.50"), new BigDecimal("120000.00"), new BigDecimal("80000.00")};
        BigDecimal[] oddValuesX = new BigDecimal[] {new BigDecimal("2.00"), new BigDecimal("60000.00"), new BigDecimal("30000.00")};
        BigDecimal[] oddValues2 = new BigDecimal[] {new BigDecimal("2.20"), new BigDecimal("90000.00"), new BigDecimal("40000.00")};

        addMatchOdds(match, oddValues1, oddValuesX, oddValues2);
    }

    private void addMatchOdds(Match match, BigDecimal[] oddValues1, BigDecimal[] oddValuesX, BigDecimal[] oddValues2) {
        Map<MatchResult, MatchOdds> matchOdds =  new HashMap<>(
                Map.of(
                        MatchResult.MS1, MatchOdds.builder()
                                .match(match)
                                .resultType(MatchResult.MS1)
                                .oddsValue(oddValues1[0])
                                .riskLimit(oddValues1[1])
                                .currentRisk(oddValues1[2])
                                .updatedAt(LocalDateTime.now())
                                .build(),
                        MatchResult.MSX, MatchOdds.builder()
                                .match(match)
                                .resultType(MatchResult.MSX)
                                .oddsValue(oddValuesX[0])
                                .riskLimit(oddValuesX[1])
                                .currentRisk(oddValuesX[2])
                                .updatedAt(LocalDateTime.now())
                                .build(),
                        MatchResult.MS2, MatchOdds.builder()
                                .match(match)
                                .resultType(MatchResult.MS2)
                                .oddsValue(oddValues2[0])
                                .riskLimit(oddValues2[1])
                                .currentRisk(oddValues2[2])
                                .updatedAt(LocalDateTime.now())
                                .build()
                )
        );

        for (MatchResult matchResult: MatchResult.values()) {
            matchOddRepository.save(matchOdds.get(matchResult));
        }
    }
}