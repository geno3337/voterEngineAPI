package com.example.voter_engine.Entity.expection;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class resourceNotFound extends RuntimeException{
    String message;
    public  resourceNotFound(String msg) {
        super(msg);
        this.message = msg;
    }

//    @ExceptionHandler(value = resourceNotFound.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public errorResponse handleException(resourceNotFound resourceNotFound){
//        return new errorResponse(resourceNotFound.getMessage(),HttpStatus.NOT_FOUND.value());
//    }
}
