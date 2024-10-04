package com.ojt.klb.exception;

public class TransactionException extends GlobalException{
    public TransactionException(String message) {
        super(message, GlobalErrorCode.BAD_REQUEST);
    }
}
