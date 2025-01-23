package com.mindera.mindswap.education_manager.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for creating a new course.
 * 
 * This class contains the necessary fields and validation rules
 * for creating a new course in the system. All fields are validated
 * using Jakarta Validation constraints.
 * 
 * Validation rules:
 * - Name: Required, between 1 and 50 characters
 * - Description: Required, between 1 and 1000 characters
 */
@Data
public class CreateCourseDTO {

    /**
     * Name of the course.
     * Must not be blank and must be between 1 and 50 characters long.
     */
    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;

    /**
     * Detailed description of the course.
     * Must not be blank and must be between 1 and 1000 characters long.
     */
    @NotBlank(message = "Description is required")
    @Size(min = 1, max = 1000, message = "Description must be between 1 and 1000 characters")
    private String description;
}
