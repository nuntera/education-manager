package com.mindera.mindswap.education_manager.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for updating an existing course.
 * 
 * This class contains the fields that can be updated for an existing course.
 * All fields are optional, allowing partial updates. When a field is null,
 * it indicates that the field should not be updated.
 * 
 * Validation rules:
 * - Name: Optional, maximum 50 characters if provided
 * - Description: Optional, maximum 1000 characters if provided
 */
@Data
public class UpdateCourseDTO {
    
    /**
     * Updated name for the course.
     * Optional field, maximum 50 characters if provided.
     */
    @Size(max = 50, message = "Name must be less than 50 characters")
    private String name;
    
    /**
     * Updated description for the course.
     * Optional field, maximum 1000 characters if provided.
     */
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;
}