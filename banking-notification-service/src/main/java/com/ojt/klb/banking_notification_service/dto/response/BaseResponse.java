package com.ojt.klb.banking_notification_service.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public abstract class BaseResponse<T> {

    @JsonProperty("success")
    private Boolean success = true;

    @JsonProperty("code")
    private Integer statusCode = ResponseMessage.SUCCESSFUL.statusCode();

    @JsonProperty("message")
    private String statusValue = ResponseMessage.SUCCESSFUL.statusCodeValue();

    @JsonProperty("executeDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime executeDate = LocalDateTime.now();

    @JsonProperty("data")
    protected T data = null;

    public void validated(T data, Integer statusCode, String statusValue) {
        if (data == null) {
            setFailure(statusCode, statusValue);
        } else {
            setData(data);
        }
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
    }

    public LocalDateTime getExecuteDate() {
        return executeDate;
    }

    public void setExecuteDate(LocalDateTime executeDate) {
        this.executeDate = executeDate;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public void setFailure(int statusCode, String statusValue) {
        this.setSuccess(false);
        this.setStatusCode(statusCode);
        this.setStatusValue(statusValue);
    }

    public void setFailure(ResponseMessage responseMessage) {
        setFailure(responseMessage.statusCode(), responseMessage.statusCodeValue());
    }

    public void setFailure(String smg) {
        setFailure(500, smg);
    }
}
