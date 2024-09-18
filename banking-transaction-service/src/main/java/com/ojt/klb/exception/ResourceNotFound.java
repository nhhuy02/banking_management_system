package com.ojt.klb.exception;

public class ResourceNotFound extends GlobalException {

    public ResourceNotFound(String errorCode, String message) {
        super(errorCode, message);
    }

    public ResourceNotFound(String message) {
        super(message);
    }
}
