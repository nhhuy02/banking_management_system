package com.ctv_it.klb.dto.filter;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
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
public class ReportAccountFilterDTO {

  @NotNull
  private String accountType;
  private RangeFilterDTO<BigDecimal> balanceRange;
  private String status;
}
