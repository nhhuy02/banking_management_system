package com.ctv_it.klb.dto;

import com.ctv_it.klb.dto.base.TransactionInfoDTO;
import java.util.List;
import java.util.UUID;

public class TransactionReportDTO {

  private UUID customerId;
  private List<TransactionInfoDTO> transactionType;
}
