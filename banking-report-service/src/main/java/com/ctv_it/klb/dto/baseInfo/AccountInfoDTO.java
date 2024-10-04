package com.ctv_it.klb.dto.baseInfo;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class AccountInfoDTO {

  private int no;
  private Long id;
  private String type;
  private String name;
  private String number;
  @Default
  private String currency = "VND";
  private BigDecimal accountingBalance;
  private BigDecimal availableBalance;
  private String branch;
  private String status;
  private String openingDate;
}
