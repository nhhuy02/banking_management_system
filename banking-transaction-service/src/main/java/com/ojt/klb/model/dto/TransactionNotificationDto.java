package com.ojt.klb.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Data
public class TransactionNotificationDto {
    private String referenceNumber;
    private String fromAccountNumber;
    private String fromAccountHolderName;
    private String fromBank;
    private String toAccountNumber;
    private String toAccountHolderName;
    private String toBank;
    private BigDecimal amount;
    private String transactionType;
    private LocalDateTime transactionDate;
}
