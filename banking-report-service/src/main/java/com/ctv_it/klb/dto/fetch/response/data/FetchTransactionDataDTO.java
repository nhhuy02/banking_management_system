package com.ctv_it.klb.dto.fetch.response.data;

import com.ctv_it.klb.dto.baseInfo.TransactionInfoDTO;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class FetchTransactionDataDTO {

  private Long customerId;
  private Long accountId;
  private List<TransactionInfoDTO> transactions;
}
