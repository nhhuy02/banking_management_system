package com.app.bankingloanservice.exception;

public class InvalidLoanApplicationException extends RuntimeException {
    public InvalidLoanApplicationException(String message) {
        super(message);
    }
}
