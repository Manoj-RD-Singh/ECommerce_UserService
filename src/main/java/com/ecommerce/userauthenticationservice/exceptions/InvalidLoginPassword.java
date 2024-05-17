package com.ecommerce.userauthenticationservice.exceptions;

public class InvalidLoginPassword extends RuntimeException{
    public InvalidLoginPassword(String message){
        super(message);
    }
}
