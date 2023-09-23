package com.example.voter_engine.validation;

import com.example.voter_engine.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class userUniqueEmailValidator implements ConstraintValidator<UserUniqueEmail,String> {

    @Autowired
    private com.example.voter_engine.repository.userRepository userRepository;

    @Override
    public boolean isValid(String gmail, ConstraintValidatorContext constraintValidatorContext) {
        return !userRepository.existsByGmail(gmail);
    }
}
