package com.ctv_it.klb.dto;

import com.ctv_it.klb.dto.base.CustomerInfoDTO;
import com.ctv_it.klb.dto.base.TransactionInfoDTO;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class TransactionReportDTO {

  private CustomerInfoDTO customer;
  private List<TransactionInfoDTO> transactions;
}
