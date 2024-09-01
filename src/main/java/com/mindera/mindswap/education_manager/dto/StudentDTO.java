package com.mindera.mindswap.education_manager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class StudentDTO {

    public StudentDTO() {
    }

    public StudentDTO(Long id) {
        this.id = id;
    }

    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    private String email;
}
