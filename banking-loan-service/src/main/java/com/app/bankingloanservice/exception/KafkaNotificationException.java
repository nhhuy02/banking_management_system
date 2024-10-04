package com.app.bankingloanservice.exception;

public class KafkaNotificationException extends RuntimeException {
    public KafkaNotificationException(String message, Throwable cause) {
        super(message, cause);
    }

    public KafkaNotificationException(String message) {
        super(message);
    }
}
