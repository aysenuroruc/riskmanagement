package com.bilyoner.riskmanagement.exception;

public class InvalidBetException extends BettingException {
    public InvalidBetException(String message) {
        super(message, "INVALID_BET");
    }
}
