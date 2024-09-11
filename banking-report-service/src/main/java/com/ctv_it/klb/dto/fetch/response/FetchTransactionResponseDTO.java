package com.ctv_it.klb.dto.fetch.response;

import com.ctv_it.klb.dto.base.TransactionInfoDTO;
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
public class FetchTransactionResponseDTO {

  private UUID customerId;
  List<TransactionInfoDTO> transactions;
}
