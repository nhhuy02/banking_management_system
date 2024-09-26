package com.ojt.klb.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FundTransferRequest {

    @NotBlank(message = "From account cannot be blank")
    private String fromAccount;

    @NotBlank(message = "To account cannot be blank")
    private String toAccount;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be a positive value")
    private BigDecimal amount;
}
