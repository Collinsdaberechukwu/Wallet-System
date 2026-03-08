package com.collins.Wallet.System.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WalletAlreadyExistException extends RuntimeException{

    public WalletAlreadyExistException(String message){
        super(message);
    }
}

