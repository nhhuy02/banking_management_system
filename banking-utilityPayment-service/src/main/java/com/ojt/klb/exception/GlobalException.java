package com.ojt.klb.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
public class GlobalException extends RuntimeException{
    private String code;
    private String message;

    public GlobalException(String message) {
        super(message);
    }
}
