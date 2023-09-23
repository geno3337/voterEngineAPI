package com.example.voter_engine.expection;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class globalExceptionHandler {

    @ExceptionHandler(resourceNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody errorResponse handleException(resourceNotFound resourceNotFound){
//        errorResponse ER = new errorResponse(resourceNotFound.getMessage(),HttpStatus.NOT_FOUND.value());
        return new errorResponse(resourceNotFound.getMessage(), HttpStatus.NOT_FOUND.value());
//        return new ResponseEntity(ER, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(userNameRestriction.class)
    public ResponseEntity<errorResponse> handleNameException(userNameRestriction userNameRestriction){
        errorResponse ER=new errorResponse(userNameRestriction.getMessage(),HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity(ER,HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public List<FieldError> handleNameException(MethodArgumentNotValidException ex){
//        Map<String,String> errorMap= new HashMap<>();
////        ex.getBindingResult().getFieldErrors().forEach(error -> {errorMap.put(error.getField(),error.getDefaultMessage());});
////        errorResponse ER=new errorResponse(ex.getMessage(),HttpStatus.BAD_REQUEST.value());
////        return new ResponseEntity(ER,HttpStatus.BAD_REQUEST) ;
//         ex.getFieldErrors().forEach(error -> {errorMap.put(error.getField(),error.getDefaultMessage());});
//         return ex.getFieldErrors();
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
//        Map<String,String> errorMap= new HashMap<>();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.getViolations().add(
                    new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
//            errorMap.put(fieldError.getField(),fieldError.getDefaultMessage());
        }
        return error;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ValidationErrorResponse onConstraintValidationException(
            ConstraintViolationException e) {
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (ConstraintViolation violation : e.getConstraintViolations()) {
            error.getViolations().add(
                    new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        return error;
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    String SQLIntegrityConstraintViolationException(
            SQLIntegrityConstraintViolationException e) {
//        ValidationErrorResponse error = new ValidationErrorResponse();
//        for (ConstraintViolation violation : e.getConstraintViolations()) {
//            error.getViolations().add(
//                    new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
//        }
        return e.getMessage();
    }

    @ExceptionHandler(recordMismatchException.class)
    public ResponseEntity<errorResponse> handleRecordMismatchException(recordMismatchException recordMismatchException){
        errorResponse ER=new errorResponse(recordMismatchException.getMessage(),HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity(ER,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<errorResponse> handleAllExceptions(Exception ex, WebRequest request) {
        ex.printStackTrace();
        errorResponse response = new errorResponse( ex.getMessage(), 500);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<errorResponse> handleRuntimeException(RuntimeException ex){
        errorResponse response=new errorResponse(ex.getMessage(),500);
        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    @ExceptionHandler(MailException.class)
//    public ResponseEntity<errorResponse> handleMailException(RuntimeException ex){
//        errorResponse response=new errorResponse(ex.getMessage(),500);
//        return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(MailException.class)
//    public String handleMailException(MailException ex) {
//        // Handle the mail exception here, log it, and return a meaningful response to the client.
//        return "Email could not be sent: " + ex.getMessage();
//    }


}
