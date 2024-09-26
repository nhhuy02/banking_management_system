package com.app.bankingloanservice.exception;

/**
 * Exception thrown when a LoanType is not found.
 */
public class LoanTypeNotFoundException extends RuntimeException {

    public LoanTypeNotFoundException(String message) {
        super(message);
    }

    public LoanTypeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
