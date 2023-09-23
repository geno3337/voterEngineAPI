package com.example.voter_engine.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class EventNoValidator implements ConstraintValidator<EventNo, String> {

    List<String> list= Arrays.asList("1", "2", "3");


    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return list.contains(value);
    }
}
