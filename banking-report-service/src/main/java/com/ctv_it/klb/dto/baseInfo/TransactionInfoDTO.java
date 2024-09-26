package com.ctv_it.klb.dto.baseInfo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class TransactionInfoDTO {

  private Long id;
  private Long sourceAccountId;
  private Long destinationAccountId;
  private LocalDateTime date;
  private BigDecimal amount;
  private BigDecimal balanceBeforeTransaction;
  private BigDecimal balanceAfterTransaction;
  private String type;  // deposit, withdraw...
  private String category; // shopping, education, sport...
  private String description;
  private String status;
  private LocalDateTime updatedAt;
  private String note;
  private BigDecimal fee;
  private String transactionChanel;
}
