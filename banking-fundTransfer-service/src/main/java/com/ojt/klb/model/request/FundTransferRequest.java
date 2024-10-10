package com.ojt.klb.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
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

    @NotNull(message = "From account cannot be null")
    private String fromAccount;

    @NotNull(message = "To account cannot be null")
    private String toAccount;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be a positive value")
    private BigDecimal amount;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    String description;
}
