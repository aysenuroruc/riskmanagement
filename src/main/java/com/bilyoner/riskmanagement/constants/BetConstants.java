package com.bilyoner.riskmanagement.constants;

import java.math.BigDecimal;

public final class BetConstants {
    private BetConstants() {}

    public static final String ERR_SELECTION_REQUIRED = "At least one selection is required";
    public static final String ERR_BET_AMOUNT = "Bet amount must be greater than zero";
    public static final String ERR_DUPLICATE_RESULT = "You can't bet to same match result more than one";
    public static final String ERR_RISK_LIMIT = "Risk limits are exceeded";
    public static final String ERR_MATCH_NOT_FOUND = "Match not found";
    public static final BigDecimal ONE = BigDecimal.ONE;
    public static final BigDecimal ZERO = BigDecimal.ZERO;
    public static final int ODDS_SCALE = 2;
    public static final int DIVIDE_SCALE = 4;
    public static final String PAYOUT_RATIO = "1.0000";
}

