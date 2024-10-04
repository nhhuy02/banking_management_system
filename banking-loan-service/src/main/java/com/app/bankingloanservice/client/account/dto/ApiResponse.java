package com.app.bankingloanservice.client.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;
    private String message;
    private boolean success;
    private T data;
}
