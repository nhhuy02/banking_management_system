package com.app.bankingloanservice.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponseWrapper<T> {

    @Schema(description = "HTTP status code of the response", example = "201")
    private int status;

    @Schema(description = "Indicates whether the operation was successful", example = "true")
    private boolean success;

    @Schema(description = "Message accompanying the status code", example = "User registered successfully")
    private String message;

    @Schema(description = "Data returned in the response")
    private T data;

}

