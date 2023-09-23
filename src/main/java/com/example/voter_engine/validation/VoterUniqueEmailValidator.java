package com.example.voter_engine.validation;

import com.example.voter_engine.repository.voterListRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VoterUniqueEmailValidator implements ConstraintValidator<VoterUniqueEmail,String> {
    @Autowired
    private  voterListRepository voterListRepository;

    @Override
    public boolean isValid(String gmail, ConstraintValidatorContext constraintValidatorContext) {
        return  !voterListRepository.existsByGmail(gmail);
    }
}
