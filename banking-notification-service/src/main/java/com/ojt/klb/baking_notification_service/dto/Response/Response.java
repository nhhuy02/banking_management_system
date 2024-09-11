package com.ojt.klb.baking_notification_service.dto.Response;

import com.ojt.klb.baking_notification_service.core.StringUtils;

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
        }catch (Exception e){
            System.out.println(msgValue);
            //just warning
        }
        this.setFailure(msg.statusCode(), msgValue);
        return this;
    }

    public Response<D> withError(Integer statusCode, String message) {
        try {
            if (StringUtils.stringNotNullOrEmpty(message) && statusCode != null) {
                this.setFailure(statusCode, message);
            }
        }catch (Exception e){
            System.out.println(message);
            //just warning
        }
        return this;
    }

    public Response<D> withError(String msg) {
        this.setFailure(msg);
        return this;
    }
}
