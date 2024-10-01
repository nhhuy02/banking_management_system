package com.ctv_it.klb.dto.baseInfo;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class AccountInfoDTO {

  private Long id;
  private String type;
  private String name;
  private String number;
  private String currency;
  private BigDecimal accountingBalance;
  private BigDecimal availableBalance;
  private String branch;
  private String status;
  private LocalDate openingDate;
}
