package com.app.bankingloanservice.exception;

public class CollateralNotFoundException extends RuntimeException {

    public CollateralNotFoundException(String message) {
        super(message);
    }

    public CollateralNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
