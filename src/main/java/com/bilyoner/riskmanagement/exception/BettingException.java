package com.bilyoner.riskmanagement.exception;

import lombok.Getter;

@Getter
public class BettingException extends RuntimeException {
    private final String errorCode;

    public BettingException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
