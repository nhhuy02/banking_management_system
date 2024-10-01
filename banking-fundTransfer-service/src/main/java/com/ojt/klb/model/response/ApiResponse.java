package com.ojt.klb.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponse<T> {

    private int status;
    private String message;
    private boolean success;
    private T data;

    public ApiResponse(int status, String message, boolean success) {
        this.status = status;
        this.message = message;
        this.success = success;
    }
}


