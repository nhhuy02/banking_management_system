package com.ojt.klb.response;

import lombok.Data;

import java.math.BigDecimal;

import com.ojt.klb.model.Account;

@Data
public class AccountResponse {
    private Long id;
    private Long customerId;
    private String accountType;
    private BigDecimal balance;
    private String status;

    public AccountResponse(Account account) {
        this.id = account.getId();
        this.customerId = account.getCustomerId();
        this.accountType = account.getType();
        this.balance = account.getBalance();
        this.status = account.getStatus();
    }
}
