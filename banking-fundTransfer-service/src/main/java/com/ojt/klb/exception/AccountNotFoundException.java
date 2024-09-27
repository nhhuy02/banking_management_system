package com.ojt.klb.exception;

public class AccountNotFoundException extends GlobalException{
    public AccountNotFoundException(String errorCode, String message) {
        super(message, errorCode);
    }
}
