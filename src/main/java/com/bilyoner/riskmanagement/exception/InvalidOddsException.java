package com.bilyoner.riskmanagement.exception;

public class InvalidOddsException extends BettingException {
    public InvalidOddsException(String message) {
        super(message, "INVALID_ODDS");
    }
}
