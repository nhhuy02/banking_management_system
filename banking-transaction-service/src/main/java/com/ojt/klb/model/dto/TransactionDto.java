package com.ojt.klb.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDto {
    private String referenceNumber;

    private String accountId;

    private String transactionType;

    private BigDecimal amount;

    private LocalDateTime transactionDate;

    private String status;

    private String description;
}
