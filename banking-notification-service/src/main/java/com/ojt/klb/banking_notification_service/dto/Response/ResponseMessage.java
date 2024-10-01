package com.ojt.klb.banking_notification_service.dto.Response;

public enum ResponseMessage {
    SUCCESSFUL(200, "OK"),
    ERROR(205, "ERROR"),
    REGISTER(201, "REGISTER"),
    PAYMENT_RECEIPT(202, "PAYMENT RECEIPT"),
    PAYMENT_REMINDER(203, "PAYMENT REMINDER"),
    OTP(206, "OTP"),
    VALIDATED_ERROR(2002, "VALIDATED_ERROR"),
    VND(204, " VND"),
    NO_REPLY(205, "[NoReply-JD]"),
    BALANCE_CHANGE(206, "BALANCE_CHANGE"),
    INCREASE(207, "+"),
    DECREASE(208, "-"),
    CONTENT_BALANCE(209, "Biến động số dư: TK "),
    BALANCE(210, "Số dư "),
    LOAN(211, "LOAN APPLICATION "),
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
