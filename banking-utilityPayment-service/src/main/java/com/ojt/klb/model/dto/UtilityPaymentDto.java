package com.ojt.klb.model.dto;

import com.ojt.klb.model.TransactionStatus;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UtilityPaymentDto {
    private Long providerId;
    private BigDecimal amount;
    private String referenceNumber;
    private String account;
    private TransactionStatus status;
}
