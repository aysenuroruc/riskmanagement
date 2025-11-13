package com.bilyoner.riskmanagement.model.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ErrorResponseDTO {
    Integer status;
    String message;
    String error;
    String path;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime timestamp = LocalDateTime.now();
    List<ValidationError> validationErrors;

    @Data
    @Builder
    public static class ValidationError {
        String field;
        String message;
        Object rejectedValue;
    }
}
