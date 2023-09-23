package com.example.voter_engine.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Constraint(validatedBy = EventNoValidator.class)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface EventNo {

    public String message() default "your status must be either 1 or 2 or 3 ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
