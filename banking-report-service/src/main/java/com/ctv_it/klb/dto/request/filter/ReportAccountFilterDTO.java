package com.ctv_it.klb.dto.request.filter;

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

  private String accountType;
  private RangeFilterDTO<Double> balanceRange;
  private String status;
}
