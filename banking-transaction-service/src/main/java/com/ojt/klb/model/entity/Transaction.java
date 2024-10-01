package com.ojt.klb.model.entity;

import com.ojt.klb.model.TransactionStatus;
import com.ojt.klb.model.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Data
@Entity
@NoArgsConstructor @AllArgsConstructor
@Table(name = "transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String referenceNumber;

    private Long accountNumber;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private BigDecimal amount;

    @CreationTimestamp
    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private String description;
}
