package com.ojt.klb.exception;

public class InsufficientBalance extends GlobalException{
    public InsufficientBalance(String errorCode, String message) {
        super(message, errorCode);
    }
}