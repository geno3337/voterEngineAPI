package com.example.voter_engine.Entity.expection;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class ValidationErrorResponse {


        private List<Violation> violations = new ArrayList<>();

}
