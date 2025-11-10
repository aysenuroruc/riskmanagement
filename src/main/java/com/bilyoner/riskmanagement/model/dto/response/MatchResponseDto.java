package com.bilyoner.riskmanagement.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MatchResponseDto {
    private Long id;

    private String homeTeam;

    private String awayTeam;

    @Schema(example = "Galatasaray - Göztepe")
    private String matchName;

    private LocalDateTime matchDate;

    private List<MatchOddsResponseDto> odds;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
