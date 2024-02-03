package com.example.voter_engine.Entity.expection;

import lombok.Data;

@Data
public class Violation {

    private final String fieldName;

    private final String message;
}
