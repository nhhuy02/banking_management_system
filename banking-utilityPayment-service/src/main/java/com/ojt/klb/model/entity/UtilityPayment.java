package com.ojt.klb.model.entity;

import com.ojt.klb.model.TransactionStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "utility_payment")
@Builder
public class UtilityPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long providerId;
    private BigDecimal amount;
    private String referenceNumber;
    private String account;
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

}
