package com.ojt.klb.banking_notification_service.dto.consumer.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountData {
    private Long customerId;
    private String customerName;
    private String email;
    private Long accountNumber;
    private String accountName;
    private String phoneNumber;
}
