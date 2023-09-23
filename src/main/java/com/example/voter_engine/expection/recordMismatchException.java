package com.example.voter_engine.expection;

public class recordMismatchException extends RuntimeException{
    private String message;
    public recordMismatchException(String message) {
        super(message);
        this.message=message;
    }
}
