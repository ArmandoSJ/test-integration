package com.developer.unittest.exceptions;

public class InsufficientMoneyException extends RuntimeException {
    public InsufficientMoneyException(String message){
        super(message);
    }
}
