package com.ctv_it.klb.dto;

import com.ctv_it.klb.dto.baseInfo.AccountInfoDTO;
import com.ctv_it.klb.dto.baseInfo.CustomerInfoDTO;
import com.ctv_it.klb.dto.baseInfo.TransactionInfoDTO;
import java.math.BigDecimal;
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
  private AccountInfoDTO account;
  private BigDecimal openingBalance;
  private BigDecimal credit;
  private BigDecimal debit;
  private BigDecimal endBalance;
  private List<TransactionInfoDTO> transactions;
}
