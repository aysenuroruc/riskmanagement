package com.bilyoner.riskmanagement.domain.builder;

import com.bilyoner.riskmanagement.domain.MatchResult;
import com.bilyoner.riskmanagement.domain.entity.Match;
import com.bilyoner.riskmanagement.domain.entity.MatchOdds;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class MatchBuilder {
    public Match buildMatch(
            String homeTeam,
            String awayTeam,
            LocalDateTime matchDate,
            Map<MatchResult, BigDecimal> oddsValues,
            Map<MatchResult, BigDecimal> limits,
            Map<MatchResult, BigDecimal> currentRisks) {
        Match match = Match.builder()
                .homeTeam(homeTeam)
                .awayTeam(awayTeam)
                .matchDate(matchDate)
                .build();

        // Create odds for all three possible results
        for (MatchResult result : MatchResult.values()) {
            MatchOdds odds = MatchOdds.builder()
                    .match(match)
                    .resultType(result)
                    .oddsValue(oddsValues.get(result))
                    .riskLimit(limits.get(result))
                    .currentRisk(currentRisks.getOrDefault(result, BigDecimal.ZERO))
                    .build();
            match.addOdds(odds);
        }
        return match;
    }

    public Match buildMatchWithDefaults(
            String homeTeam,
            String awayTeam,
            LocalDateTime matchDate,
            Map<MatchResult, BigDecimal> oddsValues,
            Map<MatchResult, BigDecimal> limits) {

        Map<MatchResult, BigDecimal> defaultRisks = Map.of(
                MatchResult.MS1, BigDecimal.ZERO,
                MatchResult.MSX, BigDecimal.ZERO,
                MatchResult.MS2, BigDecimal.ZERO
        );
        return buildMatch(homeTeam, awayTeam, matchDate, oddsValues, limits, defaultRisks);
    }
}
