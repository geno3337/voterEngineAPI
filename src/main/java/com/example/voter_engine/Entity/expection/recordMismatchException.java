package com.example.voter_engine.Entity.expection;

public class recordMismatchException extends RuntimeException{
    private String message;
    public recordMismatchException(String message) {
        super(message);
        this.message=message;
    }
}
