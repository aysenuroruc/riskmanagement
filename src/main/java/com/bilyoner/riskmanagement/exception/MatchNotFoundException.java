package com.bilyoner.riskmanagement.exception;

public class MatchNotFoundException extends BettingException {

    public MatchNotFoundException(Long matchId) {
        super(String.format("Match not found with id: %d", matchId), "MATCH_NOT_FOUND");
    }
}
