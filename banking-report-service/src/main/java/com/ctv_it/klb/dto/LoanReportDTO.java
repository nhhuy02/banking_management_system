package com.ctv_it.klb.dto;

import com.ctv_it.klb.dto.base.CustomerInfoDTO;
import com.ctv_it.klb.dto.base.LoanInfoDTO;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class LoanReportDTO {

  private CustomerInfoDTO customer;
  private List<LoanInfoDTO> loans;
}
