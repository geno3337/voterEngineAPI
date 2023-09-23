package com.example.voter_engine.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Documented
@Constraint(validatedBy = userUniqueEmailValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface UserUniqueEmail {

    public String message() default "gmail is already exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
