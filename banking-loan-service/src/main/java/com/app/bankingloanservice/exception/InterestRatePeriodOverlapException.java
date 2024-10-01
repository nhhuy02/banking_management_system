package com.app.bankingloanservice.exception;

/**
 * Exception thrown when there is an overlap in interest rate periods.
 */
public class InterestRatePeriodOverlapException extends RuntimeException {
    public InterestRatePeriodOverlapException(String message) {
        super(message);
    }
}
