package com.ojt.klb.exception;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class GlobalException extends RuntimeException{

    private String errorCode;

    private String message;

    public GlobalException(String message) {
        this.message = message;
    }
}
