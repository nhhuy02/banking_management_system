package com.ctv_it.klb.dto.fetch.response.data;

import com.ctv_it.klb.dto.baseInfo.LoanInfoDTO;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class FetchLoanDataDTO {

  private Long customerId;
  private Long accountId;
  private List<LoanInfoDTO> loans;
}
