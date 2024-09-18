package com.ojt.klb.baking_notification_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerData {
    private String customerId;
    private String customerName;
    private String email;
    private String otpCode;
}
