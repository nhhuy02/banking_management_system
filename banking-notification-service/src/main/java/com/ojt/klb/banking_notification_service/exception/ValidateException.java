package com.ojt.klb.banking_notification_service.exception;

import com.ojt.klb.banking_notification_service.dto.response.ResponseMessage;

public class ValidateException  extends RuntimeException{
    private ResponseMessage msg;
    private String customMsg;

    public ValidateException(ResponseMessage msg) {
        this.msg = msg;
    }

    public ValidateException(ResponseMessage msg, String... args) {
        this.msg = msg;
        try {
            if (args != null && args.length > 0 && msg.statusCodeValue().contains("%s")) {
                this.customMsg = String.format( msg.statusCodeValue(), args);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String getMessage(){
        if (customMsg != null && !customMsg.trim().equals("")){
            return customMsg;
        }
        return msg.statusCodeValue();
    }

    public int getStaus(){
        return msg.statusCode();
    }

    public ResponseMessage getMsg() {
        return msg;
    }

    public void setMsg(ResponseMessage msg) {
        this.msg = msg;
    }
}
