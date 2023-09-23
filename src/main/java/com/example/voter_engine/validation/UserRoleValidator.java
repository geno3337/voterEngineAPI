package com.example.voter_engine.validation;

import com.example.voter_engine.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserRoleValidator  implements ConstraintValidator<UserRole,String> {

    private List<String> Roles= Arrays.asList("user","admin");

    @Override
    public boolean isValid(String role, ConstraintValidatorContext constraintValidatorContext) {
        return Roles.contains(role);
    }
}
