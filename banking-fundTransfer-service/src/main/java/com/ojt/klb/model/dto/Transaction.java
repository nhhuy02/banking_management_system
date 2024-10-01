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

    private Long accountNumber;

    private String transactionType;

    private BigDecimal amount;

    private String description;
}
