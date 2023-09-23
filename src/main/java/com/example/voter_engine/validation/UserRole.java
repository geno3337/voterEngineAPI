package com.example.voter_engine.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Constraint(validatedBy = UserRoleValidator.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface UserRole {

    public String message() default "your role must be either user or admin";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
