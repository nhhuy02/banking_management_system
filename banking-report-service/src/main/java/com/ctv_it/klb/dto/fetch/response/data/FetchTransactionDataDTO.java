package com.ctv_it.klb.dto.fetch.response.data;

import com.ctv_it.klb.dto.baseInfo.TransactionInfoDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class FetchTransactionDataDTO {

  private Long customerId;
  private Long accountId;
  private List<TransactionInfoDTO> transactions;
}
