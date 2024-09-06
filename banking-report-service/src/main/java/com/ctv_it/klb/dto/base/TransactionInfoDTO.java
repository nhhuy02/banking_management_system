package com.ctv_it.klb.dto.base;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TransactionInfoDTO {

  private UUID uuid;
  private LocalDateTime date;
  private double amount;
  private double balanceAfterTransaction;
  private String transactionType;
  private String description;
}
