package com.post.application.Post.Application.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> ConstraintViolationException(MethodArgumentNotValidException ex) {
        Map<String, String> errorMap = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = ((FieldError) error).getField();
            String defaultMessage = error.getDefaultMessage();
            errorMap.put(field, defaultMessage);
        });
        return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> GlobalExceptionHandler(Exception ex){
        return new ResponseEntity<>(new ApiResponse(ex.getMessage(),HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);

    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> RuntimeExceptionHandler(RuntimeException ex){
        return new ResponseEntity<>(new ApiResponse(ex.getMessage(),HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> IOExceptionHandler(IOException ex){
        return new ResponseEntity<>(new ApiResponse(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}