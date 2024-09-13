package com.ctv_it.klb.dto.base;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class TransactionInfoDTO {

  private UUID id;
  private AccountInfoDTO sourceAccount;
  private AccountInfoDTO destinationAccount;
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
