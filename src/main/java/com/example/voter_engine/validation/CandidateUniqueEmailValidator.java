package com.example.voter_engine.validation;

import com.example.voter_engine.repository.candidateListRepository;
import com.example.voter_engine.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CandidateUniqueEmailValidator  implements ConstraintValidator<CandidateUniqueEmail,String> {

    @Autowired
    private com.example.voter_engine.repository.candidateListRepository candidateListRepository;

    @Override
    public boolean isValid(String gmail, ConstraintValidatorContext constraintValidatorContext) {
        return !candidateListRepository.existsByGmail(gmail);
    }
}
