package com.ctv_it.klb.dto.baseInfo;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@ToString
public class AccountInfoDTO {

  private Long id;
  private String type;
  private String number;
  private String currency;
  private BigDecimal accountingBalance;
  private BigDecimal availableBalance;
  private String branch;
  private String status;
  private LocalDate openingDate;
}
