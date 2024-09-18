package com.ojt.klb.baking_notification_service.dto.Response;

public enum ResponseMessage {
    SUCCESSFUL(200, "OK"),
    ERROR(205, "ERROR"),
    REGISTER(201, "REGISTER"),
    VALIDATED_ERROR(2002, "VALIDATED_ERROR"),
    ;
    private final int statusCode;
    private final String statusCodeValue;

    ResponseMessage(int statusCode, String statusCodeValue) {
        this.statusCode = statusCode;
        this.statusCodeValue = statusCodeValue;
    }

    public int statusCode() {
        return statusCode;
    }

    public String statusCodeValue() {
        return statusCodeValue;
    }

    public static ResponseMessage getByStatus(int statusCode) {
        for (ResponseMessage e : values()) {
            if (e.statusCode == statusCode) return e;
        }
        return null;
    }
}
