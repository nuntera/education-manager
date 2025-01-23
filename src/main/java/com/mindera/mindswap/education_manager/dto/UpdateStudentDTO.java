package com.mindera.mindswap.education_manager.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateStudentDTO {

    @Size(max = 50, message = "First name must be less than 50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name must be less than 50 characters")
    private String lastName;

    @Size(max = 100, message = "Email must be less than 100 characters")
    private String email;
}