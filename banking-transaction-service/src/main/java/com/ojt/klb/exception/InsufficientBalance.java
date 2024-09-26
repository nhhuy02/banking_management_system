package com.ojt.klb.exception;

public class InsufficientBalance extends GlobalException {

    public InsufficientBalance(String message) {
        super(message, GlobalErrorCode.BAD_REQUEST);
    }
}

