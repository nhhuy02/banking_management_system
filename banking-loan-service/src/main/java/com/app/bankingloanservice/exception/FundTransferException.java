package com.app.bankingloanservice.exception;

public class FundTransferException extends RuntimeException {
    public FundTransferException(String message, Throwable cause) {
        super(message, cause);
    }
    public FundTransferException(String message) {
        super(message);
    }
}
