package com.mindera.mindswap.education_manager.dto;

import lombok.Data;

/**
 * Data Transfer Object (DTO) for representing student information.
 * 
 * This class is used to transfer student data between the service layer
 * and the client, excluding sensitive or unnecessary information from
 * the entity class.
 * 
 * Properties:
 * - id: Unique identifier of the student
 * - firstName: Student's first name
 * - lastName: Student's last name
 * - email: Student's email address
 */
@Data
public class StudentDTO {
    /**
     * Unique identifier of the student.
     */
    private Long id;

    /**
     * Student's first name.
     */
    private String firstName;

    /**
     * Student's last name.
     */
    private String lastName;

    /**
     * Student's email address.
     */
    private String email;
}
