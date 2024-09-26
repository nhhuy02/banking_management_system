package com.app.bankingloanservice.exception;

/**
 * Exception thrown when a document is not found.
 */
public class DocumentNotFoundException extends RuntimeException {

    public DocumentNotFoundException(String message) {
        super(message);
    }

    public DocumentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
