package com.ojt.klb.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ojt.klb.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ErrorResponseHandler {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void setErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ApiResponse<Object> apiResponse = new ApiResponse<>();
        apiResponse.setStatus(status);
        apiResponse.setMessage(message);
        apiResponse.setSuccess(false);
        apiResponse.setData(null);

        String jsonResponse = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(jsonResponse);
    }
}
