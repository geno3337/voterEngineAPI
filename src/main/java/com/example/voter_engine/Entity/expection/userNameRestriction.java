package com.example.voter_engine.Entity.expection;

public class userNameRestriction extends RuntimeException{

    private String message;

    public userNameRestriction(String message) {
        super(message);
        this.message=message;
    }
}
