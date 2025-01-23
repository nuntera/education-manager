package com.mindera.mindswap.education_manager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Error response containing details about the failure")
public class ErrorResponseDTO {

    @Schema(description = "Error message describing what went wrong")
    private String message;

    // Constructor for message-only errors
    public ErrorResponseDTO(String message) {
        this.message = message;
    }
}