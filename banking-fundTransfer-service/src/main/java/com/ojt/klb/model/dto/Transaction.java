package com.ojt.klb.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transaction {

    private String accountNumber;

    private String toAccount;

    private String toAccountName;

    private String transactionType;

    private BigDecimal amount;

    private BigDecimal balanceBeforeTransaction;

    private BigDecimal balanceAfterTransaction;

    private BigDecimal fee;

    private String description;
}
