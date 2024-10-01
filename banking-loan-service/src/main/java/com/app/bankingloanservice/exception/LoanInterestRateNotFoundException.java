package com.app.bankingloanservice.exception;

/**
 * Exception thrown when no interest rate is found for a loan.
 */
public class LoanInterestRateNotFoundException extends RuntimeException {
    public LoanInterestRateNotFoundException(String message) {
        super(message);
    }
}