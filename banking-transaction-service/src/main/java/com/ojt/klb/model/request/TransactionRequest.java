package com.ojt.klb.model.request;

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
public class TransactionRequest {

    private String referenceNumber;

    private String accountNumber;

    private String transactionType;

    private BigDecimal amount;

    private LocalDateTime localDateTime;

    private String transactionStatus;

    private String description;
}