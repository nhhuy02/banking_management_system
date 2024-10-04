package com.app.bankingloanservice.exception;

public class InvalidRepaymentException extends RuntimeException {
    public InvalidRepaymentException(String message) {
        super(message);
    }
}
