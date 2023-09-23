package com.example.voter_engine.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Constraint(validatedBy = VoterUniqueEmailValidator.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface VoterUniqueEmail {

    public String message() default "email already exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
