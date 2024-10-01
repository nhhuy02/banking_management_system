package com.ojt.klb.kafka;

import com.ojt.klb.model.external.Account;
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

    public TransactionNotification(String referenceNumber, Account account, String transactionType, BigDecimal amount, LocalDateTime now, String description) {
    }
}
