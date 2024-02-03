package com.example.voter_engine.validation;

import com.example.voter_engine.repository.CandidateRepository;
import com.example.voter_engine.repository.candidateListRepository;
import com.example.voter_engine.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CandidateUniqueEmailValidator  implements ConstraintValidator<CandidateUniqueEmail,String> {

    @Autowired
    private com.example.voter_engine.repository.candidateListRepository candidateListRepository;

    @Autowired
    private CandidateRepository candidateRepository;
    @Override
    public boolean isValid(String gmail, ConstraintValidatorContext constraintValidatorContext) {
        return !(candidateListRepository.existsByGmail(gmail) || candidateRepository.existsByGmail(gmail));
    }
}
