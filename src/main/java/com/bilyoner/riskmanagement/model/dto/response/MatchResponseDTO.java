package com.bilyoner.riskmanagement.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MatchResponseDTO {
    Long id;
    String homeTeam;
    String awayTeam;
    String matchName;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime matchDate;

    List<MatchOddsResponseDTO> odds;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime updatedAt;

}
