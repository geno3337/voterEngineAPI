package com.example.voter_engine.expection;

import lombok.Data;

@Data
public class Violation {

    private final String fieldName;

    private final String message;
}
