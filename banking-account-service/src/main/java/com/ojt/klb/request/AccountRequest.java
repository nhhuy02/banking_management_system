package com.ojt.klb.request;

import lombok.Data;

@Data
public class AccountRequest {
    private Long customerId;
    private String accountType;
}
