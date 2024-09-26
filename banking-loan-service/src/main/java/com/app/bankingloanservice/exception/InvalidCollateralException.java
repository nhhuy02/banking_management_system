package com.app.bankingloanservice.exception;

public class InvalidCollateralException extends RuntimeException {
    public InvalidCollateralException(String message) {
        super(message);
    }

    public InvalidCollateralException(String message, Throwable cause) {
        super(message, cause);
    }
}
