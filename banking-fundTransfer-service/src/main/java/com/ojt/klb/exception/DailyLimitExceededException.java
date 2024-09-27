package com.ojt.klb.exception;

public class DailyLimitExceededException extends GlobalException{
    public DailyLimitExceededException(String errorCode, String message) {
        super(message, errorCode);
    }
}
