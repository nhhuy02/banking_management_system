package com.ctv_it.klb.dto.fetch.response;

import com.ctv_it.klb.dto.base.LoanInfoDTO;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class FetchLoanResponseDTO {

  private UUID customerId;
  private UUID accountId;
  List<LoanInfoDTO> loans;
}
