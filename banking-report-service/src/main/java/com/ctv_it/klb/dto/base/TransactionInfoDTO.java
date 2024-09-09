package com.ctv_it.klb.dto.base;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
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
  private BigDecimal amount;
  private BigDecimal balanceAfterTransaction;
  private String type;
  private String category;
  private String description;
}
