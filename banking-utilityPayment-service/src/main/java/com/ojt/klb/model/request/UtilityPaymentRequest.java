package com.ojt.klb.model.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UtilityPaymentRequest {
    @NotNull(message = "provider id can not be not blank")
    private Long providerId;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be a positive value")
    private BigDecimal amount;

    @NotNull(message = "account number cannot be null")
    private String account;

    private String description;
}
