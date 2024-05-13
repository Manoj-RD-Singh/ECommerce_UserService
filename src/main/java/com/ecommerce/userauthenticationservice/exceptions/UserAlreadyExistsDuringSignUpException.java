package com.ecommerce.userauthenticationservice.exceptions;

public class UserAlreadyExistsDuringSignUpException extends RuntimeException{
    public UserAlreadyExistsDuringSignUpException(String message){
        super(message);
    }
}
