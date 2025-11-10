package com.bilyoner.riskmanagement.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BetResponseDto {

    private Long id;
    private BigDecimal betAmount;

    @Schema(description = "Total odds (multiplied)", example = "3.63")
    private BigDecimal totalOdds;

    private BigDecimal potentialWin;
    private String status;
    private Integer selectionCount;
    private List<BetSelectionResponseDto> selections;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

}
