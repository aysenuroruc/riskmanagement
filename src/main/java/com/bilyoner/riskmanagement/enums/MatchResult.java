package com.bilyoner.riskmanagement.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchResult {
    MS1("1", "Home Team Wins"),
    MSX("X", "Draw"),
    MS2("2", "Away Team Wins");

    private final String code;
    private final String description;

    public static MatchResult fromCode(String code) {
        for (MatchResult result : values()) {
            if (result.getCode().equalsIgnoreCase(code)) {
                return result;
            }
        }
        throw new IllegalArgumentException("Invalid match result code: " + code);
    }

    @JsonValue
    public String toJson() {
        return code;
    }
}
