package com.mindera.mindswap.education_manager.dto;

import com.mindera.mindswap.education_manager.validation.UniqueEmail;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for creating a new student.
 * 
 * This class contains the necessary fields and validation rules
 * for creating a new student in the system. All fields are validated
 * using Jakarta Validation constraints.
 * 
 * Validation rules:
 * - First Name: Required, minimum 2 characters, maximum 50 characters
 * - Last Name: Required, minimum 2 characters, maximum 50 characters
 * - Email: Required, valid format, maximum 100 characters
 */
@Data
public class CreateStudentDTO {

    /**
     * Student's first name.
     * Must not be blank and must be between 2 and 50 characters.
     */
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    /**
     * Student's last name.
     * Must not be blank and must be between 2 and 50 characters.
     */
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    /**
     * Student's email address.
     * Must not be blank and must be valid format, must be less than 100 characters.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @UniqueEmail
    private String email;
}
