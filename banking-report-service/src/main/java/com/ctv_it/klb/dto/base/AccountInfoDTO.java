package com.ctv_it.klb.dto.base;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class AccountInfoDTO {

  private UUID id;
  private String holeName;
  private String type;
  private String number;
  private String currency;
  private BigDecimal accountingBalance;
  private BigDecimal availableBalance;
  private String branch;
  private String status;
  private LocalDate openingDate;
}
