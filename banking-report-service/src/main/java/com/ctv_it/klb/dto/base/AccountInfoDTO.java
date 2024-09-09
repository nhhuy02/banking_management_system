package com.ctv_it.klb.dto.base;

import java.math.BigDecimal;
import java.time.LocalDate;
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
public class AccountInfoDTO {

  private UUID uuid;
  private String type;
  private String number;
  private BigDecimal accountingBalance;
  private String currency;
  private BigDecimal availableBalance;
  private String branch;
  private String status;
  private LocalDate openingDate;
}
