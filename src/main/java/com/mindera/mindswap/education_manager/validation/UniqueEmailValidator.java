package com.mindera.mindswap.education_manager.validation;

import com.mindera.mindswap.education_manager.repository.StudentRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        // No initialization needed for this validator
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null) {
            return true; // Let @NotNull handle null validation
        }
        
        if (studentRepository.existsByEmail(email)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("The email '" + email + "' is already in use")
                   .addConstraintViolation();
            return false;
        }
        return true;
    }
}