package com.collins.Wallet.System.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ACCEPTED)
public class PaymentSuccessfulException extends RuntimeException{

    public PaymentSuccessfulException(String msg){
        super(msg);
    }
}
