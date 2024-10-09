package com.ojt.klb.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class TransactionDto {

    @NotNull(message = "Account number cannot be null")
    private String accountNumber;

    @NotBlank(message = "Transaction type cannot be blank")
    private String transactionType;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be a positive value")
    private BigDecimal amount;

    @Size(max = 200, message = "Description cannot exceed 200 characters")
    private String description;

    @Schema(hidden = true)
    private BigDecimal balanceBeforeTransaction;

    @Schema(hidden = true)
    private BigDecimal balanceAfterTransaction;

    @Schema(hidden = true)
    private BigDecimal fee;

}
