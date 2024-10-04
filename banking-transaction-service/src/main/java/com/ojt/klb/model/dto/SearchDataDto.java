package com.ojt.klb.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchDataDto {
    @NotNull
    private String accountNumber;

    private String transactionType;

    private BigDecimal amount;

    private String description;

    private LocalDateTime transactionDate;

    private BigDecimal balance;
}
