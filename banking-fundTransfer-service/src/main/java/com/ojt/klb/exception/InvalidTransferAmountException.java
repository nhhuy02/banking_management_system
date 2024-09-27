package com.ojt.klb.exception;

public class InvalidTransferAmountException extends GlobalException {
    public InvalidTransferAmountException(String errorCode, String message) {
        super(message, errorCode);
    }
}