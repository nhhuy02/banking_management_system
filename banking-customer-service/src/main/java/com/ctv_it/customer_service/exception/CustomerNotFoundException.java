package com.ctv_it.customer_service.exception;

public class CustomerNotFoundException extends GlobalException{
    public CustomerNotFoundException(String message) {
        super(message, GlobalErrorCode.NOT_FOUND);
    }

}
