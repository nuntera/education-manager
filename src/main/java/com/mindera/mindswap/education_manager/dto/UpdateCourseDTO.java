package com.mindera.mindswap.education_manager.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateCourseDTO {
    
    @Size(max = 50, message = "Name must be less than 50 characters")
    private String name;
    
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;
} 