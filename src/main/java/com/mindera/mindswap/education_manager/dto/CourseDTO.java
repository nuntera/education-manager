package com.mindera.mindswap.education_manager.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for representing course information.
 * 
 * This class is used to transfer course data between the service layer
 * and the client, excluding sensitive or unnecessary information from
 * the entity class.
 * 
 * Properties:
 * - id: Unique identifier of the course
 * - name: Name of the course
 * - description: Detailed description of the course
 */
@Data
public class CourseDTO {
    /**
     * Unique identifier of the course.
     */
    private Long id;

    /**
     * Name of the course.
     */
    private String name;

    /**
     * Detailed description of the course.
     */
    private String description;
}
