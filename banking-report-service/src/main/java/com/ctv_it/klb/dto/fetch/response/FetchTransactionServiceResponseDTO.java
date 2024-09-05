package com.ctv_it.klb.dto.fetch.response;

import com.ctv_it.klb.dto.base.TransactionInfoDTO;
import java.util.List;
import java.util.UUID;
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
public class FetchTransactionServiceResponseDTO {

  private UUID accountId;
  List<TransactionInfoDTO> transactionInfoDTOS;
}
