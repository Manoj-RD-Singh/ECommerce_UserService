package com.ecommerce.userauthenticationservice.exceptions;


public class UserNotExistDuringLoginException extends  RuntimeException{
    public UserNotExistDuringLoginException(String message){
        super(message);
    }
}
