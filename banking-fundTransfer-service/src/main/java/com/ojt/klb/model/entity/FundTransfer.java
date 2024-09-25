package com.ojt.klb.model.entity;

import com.ojt.klb.model.TransactionStatus;
import com.ojt.klb.model.TransferType;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FundTransfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fundTransferId;

    private String transactionReference;

    private String fromAccount;

    private String toAccount;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    private TransferType transferType;

    @CreationTimestamp
    private LocalDateTime transferredOn;
}
