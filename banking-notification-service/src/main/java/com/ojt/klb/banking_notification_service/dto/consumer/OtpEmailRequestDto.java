package com.ojt.klb.banking_notification_service.dto.consumer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OtpEmailRequestDto  {

    private Long customerId;
    private String customerName;
    private String email;
    private String otpCode;
    private String timeToLiveCode;
}
