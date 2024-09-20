package com.ctv_it.klb.dto.fetch.response;

import com.ctv_it.klb.dto.base.LoanInfoDTO;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class FetchLoanDataDTO {

  private Long customerId;
  private Long accountId;
  List<LoanInfoDTO> loans;
}
