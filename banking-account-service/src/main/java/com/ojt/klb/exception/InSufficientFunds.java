package com.ojt.klb.exception;

public class InSufficientFunds extends GlobalException{
    public InSufficientFunds() {
        super("Insufficient funds", GlobalErrorCode.NOT_FOUND);
    }

    public InSufficientFunds(String message) {
        super(message, GlobalErrorCode.NOT_FOUND);
    }
}
