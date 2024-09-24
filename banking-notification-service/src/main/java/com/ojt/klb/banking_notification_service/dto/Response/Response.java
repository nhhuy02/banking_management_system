package com.ojt.klb.banking_notification_service.dto.Response;

import java.util.logging.Level;
import java.util.logging.Logger;


import com.ojt.klb.banking_notification_service.core.StringUtils;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Response<D> extends BaseResponse<D> {

    public Response<D> withData(D data) {
        this.setData(data);
        return this;
    }

    public Response<D> withError(ResponseMessage msg, Object... args) {
        String msgValue = msg.statusCodeValue();
        try {
            if (args != null && args.length > 0 && msgValue.contains("%s")) {
                msgValue = String.format(msgValue, args);
            }
        } catch (Exception e) {
            // Log the error instead of printing to standard output
            Logger.getLogger(Response.class.getName()).log(Level.WARNING, msgValue, e);
        }
        this.setFailure(msg.statusCode(), msgValue);
        return this;
    }

    public Response<D> withError(Integer statusCode, String message) {
        try {
            if (StringUtils.stringNotNullOrEmpty(message) && statusCode != null) {
                this.setFailure(statusCode, message);
            }
        } catch (Exception e) {
            // Log the error instead of printing to standard output
            Logger.getLogger(Response.class.getName()).log(Level.WARNING, message, e);
        }
        return this;
    }

    public Response<D> withError(String msg) {
        this.setFailure(msg);
        return this;
    }
}
