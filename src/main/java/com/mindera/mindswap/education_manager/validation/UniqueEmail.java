package com.mindera.mindswap.education_manager.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
@Documented
public @interface UniqueEmail {
    String message() default "This email is already registered in our system";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
} 