package com.ojt.klb.model.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchDataDto {

  private Long id;

  private String referenceNumber;

  @NotNull
  private String accountNumber;

  private String transactionType;

  private BigDecimal amount;

  private BigDecimal balanceBeforeTransaction;

  private BigDecimal balanceAfterTransaction;

  private BigDecimal fee;

  private String description;

  private LocalDateTime transactionDate;
}
