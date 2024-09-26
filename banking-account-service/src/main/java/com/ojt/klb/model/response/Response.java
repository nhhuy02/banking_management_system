package com.ojt.klb.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response {
    private int status;
    private String message;
    private boolean success;
    private Object data;

    public Response(int status, String message, boolean success) {
        this.status = status;
        this.message = message;
        this.success = success;
    }
}

