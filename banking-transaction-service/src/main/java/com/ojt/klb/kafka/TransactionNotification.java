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

    private Long customerId;

    private String accountNumber;

    private String customerName;

    private String balance;

    private String transactionType;

    private BigDecimal amount;

    private LocalDateTime localDateTime;

    private String description;

    public TransactionNotification(String referenceNumber, Long customerId, Account account, String accountName, BigDecimal balance, String transactionType, BigDecimal amount, LocalDateTime now, String description) {
    }
}
