package com.ctv_it.klb.dto;

import com.ctv_it.klb.dto.base.LoanInfoDTO;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class LoanReportDTO {

  private Long customerId;
  Map<String, LoanInfoDTO> loans; // keys = loan status
}
