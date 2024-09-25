package com.ctv_it.customer_service.dto;

import lombok.Data;

@Data
public class AccountData {
    private Long customerId;
    private String customerName;
    private String email;
    private String phoneNumber;
    private Long accountNumber;
    private String accountName;
}
