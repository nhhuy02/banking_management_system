package com.app.bankingloanservice.exception;

public class LoanRepaymentFetchException extends RuntimeException {
    public LoanRepaymentFetchException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoanRepaymentFetchException(String message) {
        super(message);
    }
}
