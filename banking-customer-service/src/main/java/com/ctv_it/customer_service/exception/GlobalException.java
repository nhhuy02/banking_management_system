package com.ctv_it.customer_service.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GlobalException extends RuntimeException {

    private String errorCode;

    private String message;

    public GlobalException(String message) {
        this.message = message;
    }
}
