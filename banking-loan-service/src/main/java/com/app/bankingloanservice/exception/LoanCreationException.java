package com.app.bankingloanservice.exception;

public class LoanCreationException extends RuntimeException {

    public LoanCreationException(String message) {
        super(message);
    }

    public LoanCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}

