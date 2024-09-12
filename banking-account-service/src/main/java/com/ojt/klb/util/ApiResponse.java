package com.ojt.klb.util;

public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
    private String error;
    private boolean isSuccess; // Thêm isSuccess

    public ApiResponse(int status, String message, T data, String error, boolean isSuccess) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.error = error;
        this.isSuccess = isSuccess; // Gán giá trị isSuccess
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }

    public boolean getIsSuccess() {
        return isSuccess;
    }

    // Tạo phương thức trả về thành công
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, "Success", data, null, true);
    }

    // Tạo phương thức trả về lỗi
    public static ApiResponse<String> error(int status, String message, String error) {
        return new ApiResponse<>(status, message, null, error, false);
    }
}
