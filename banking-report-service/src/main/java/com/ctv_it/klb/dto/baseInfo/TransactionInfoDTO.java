package com.ctv_it.klb.dto.baseInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Setter
@Getter
@ToString
public class TransactionInfoDTO {

  private Long id;
  private String referenceNumber;
  private Long accountNumber;
  private LocalDateTime transactionDate;
  private String transactionType;
  private BigDecimal balanceBeforeTransaction;
  private BigDecimal amount;
  private BigDecimal balanceAfterTransaction;
  private BigDecimal fee;
  private String description;
}
