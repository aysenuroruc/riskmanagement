package com.bilyoner.riskmanagement.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BetStatus {
    PENDING("Pending", "Bet is placed and waiting for match results"),
    WON("Won", "Bet has won"),
    LOST("Lost", "Bet has lost"),
    CANCELLED("Cancelled", "Bet has been cancelled");

    private final String displayName;
    private final String description;
}
