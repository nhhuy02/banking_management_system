package com.ctv_it.klb.dto;

import com.ctv_it.klb.dto.baseInfo.AccountInfoDTO;
import com.ctv_it.klb.dto.baseInfo.CustomerInfoDTO;
import com.ctv_it.klb.dto.baseInfo.LoanInfoDTO;
import com.ctv_it.klb.dto.baseInfo.LoanInfoDTO.LoanRepaymentInfoDTO;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Setter
@Getter
public class LoanRepaymentReportDTO {

  private CustomerInfoDTO customer;
  private AccountInfoDTO account;
  private LoanInfoDTO loan;
  private List<LoanRepaymentInfoDTO> loanRepayments;
}
