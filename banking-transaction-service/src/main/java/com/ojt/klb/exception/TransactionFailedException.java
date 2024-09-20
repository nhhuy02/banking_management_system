package com.ojt.klb.exception;

public class TransactionFailedException extends GlobalException {
    public TransactionFailedException(String errorCode, String message) {
        super(errorCode, message);
    }

    public TransactionFailedException(String message) {
        super(message);
    }
}
