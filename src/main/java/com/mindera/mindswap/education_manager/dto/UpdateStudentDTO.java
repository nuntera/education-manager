package com.mindera.mindswap.education_manager.dto;

import com.mindera.mindswap.education_manager.validation.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for updating an existing student.
 * 
 * This class contains the fields that can be updated for an existing student.
 * All fields are optional, allowing partial updates. When a field is null,
 * it indicates that the field should not be updated.
 * 
 * Validation rules:
 * - First Name: Optional, maximum 50 characters if provided
 * - Last Name: Optional, maximum 50 characters if provided
 * - Email: Optional, maximum 100 characters if provided
 */
@Data
public class UpdateStudentDTO {

    /**
     * Updated first name for the student.
     * Optional field, maximum 50 characters if provided.
     */
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    /**
     * Updated last name for the student.
     * Optional field, maximum 50 characters if provided.
     */
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    /**
     * Updated email address for the student.
     * Optional field, maximum 100 characters if provided.
     */
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @UniqueEmail
    private String email;
}