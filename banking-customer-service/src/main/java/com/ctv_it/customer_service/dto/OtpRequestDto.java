package com.ctv_it.customer_service.dto;

import lombok.Data;

@Data
public class OtpRequestDto {
    private Long customerId;
    private String customerName;
    private String email;
    private String otpCode;
    private String timeToLiveCode;
}
