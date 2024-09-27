package com.ojt.klb.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionNotification {
    private String referenceNumber;

    private String accountNumber;

    private String transactionType;

    private BigDecimal amount;

    private LocalDateTime localDateTime;

    private String description;
}
