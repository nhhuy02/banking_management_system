package com.ojt.klb.banking_notification_service.dto.consumer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransData {
    private String email;
    private String referenceNumber;

    private Long customerId;

    private String accountNumber;

    private String customerName;

    private BigDecimal balance;

    private String transactionType;

    private BigDecimal amount;

    private LocalDateTime localDateTime;

    private String description;
}
