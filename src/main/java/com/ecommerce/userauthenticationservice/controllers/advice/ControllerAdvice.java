package com.ecommerce.userauthenticationservice.controllers.advice;

import com.ecommerce.userauthenticationservice.exceptions.UserAlreadyExistsDuringSignUpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({Exception.class})
    public ResponseEntity<String> handleGenericException(Exception ex ){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({UserAlreadyExistsDuringSignUpException.class})
    public ResponseEntity<String> handleUserAlreadyExistsException(Exception ex){
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
