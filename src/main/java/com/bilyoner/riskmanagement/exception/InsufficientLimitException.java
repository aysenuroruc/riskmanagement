package com.bilyoner.riskmanagement.exception;

import java.math.BigDecimal;

public class InsufficientLimitException extends BettingException {

    public InsufficientLimitException(String matchName, String resultType, BigDecimal availableLimit,
                                      BigDecimal requestedAmount) {
        super(String.format("Insufficient limit for match '%s' result '%s'. Available: %.2f, Requested: %.2f",
                matchName, resultType, availableLimit, requestedAmount), "INSUFFICIENT_LIMIT");
    }
}
