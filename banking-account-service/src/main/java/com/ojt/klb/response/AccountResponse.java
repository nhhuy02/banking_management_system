package com.ojt.klb.response;

import lombok.Data;

import java.math.BigDecimal;

import com.ojt.klb.model.Account;

import java.time.LocalDate;

@Data
public class AccountResponse {
    private Long id;
    private Long customerId;
    private String type; // Changed from accountType to type
    private BigDecimal accountingBalance;
    private BigDecimal availableBalance;
    private String status;
    private String accountNumber;
    private String branch; // Added field for branch
    private LocalDate openingDate; // Added field for openingDate
    private String currency; // Added field for currency

    public AccountResponse(Account account) {
        this.id = account.getId();
        this.customerId = account.getCustomerId();
        this.type = account.getType(); // Changed from accountType to type
        this.accountingBalance = account.getAccountingBalance();
        this.availableBalance = account.getAvailableBalance();
        this.status = account.getStatus();
        this.accountNumber = account.getAccountNumber();
        this.branch = account.getBranch(); // Added branch
        this.openingDate = account.getOpeningDate(); // Added openingDate
        this.currency = account.getCurrency(); // Added currency
    }
}
