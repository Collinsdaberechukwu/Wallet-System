package com.collins.Wallet.System.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class DuplicateException extends RuntimeException{

    public DuplicateException(String message){
        super(message);
    }
}
