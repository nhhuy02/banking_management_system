package com.ctv_it.klb.service.type.impl;

import com.ctv_it.klb.dto.LoanRepaymentReportDTO;
import com.ctv_it.klb.dto.baseInfo.AccountInfoDTO;
import com.ctv_it.klb.dto.baseInfo.CustomerInfoDTO;
import com.ctv_it.klb.dto.baseInfo.LoanInfoDTO;
import com.ctv_it.klb.dto.baseInfo.LoanInfoDTO.LoanRepaymentInfoDTO;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import com.ctv_it.klb.dto.filter.extend.LoanRepaymentFilterDTO;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.fetch.FetchAccountServiceFC;
import com.ctv_it.klb.service.fetch.FetchCustomerServiceFC;
import com.ctv_it.klb.service.fetch.FetchLoanServiceFC;
import com.ctv_it.klb.service.type.ReportTypeService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanRepaymentReportServiceImpl implements ReportTypeService<LoanRepaymentReportDTO> {

  private final FetchCustomerServiceFC fetchCustomerServiceFC;
  private final FetchAccountServiceFC fetchAccountServiceFC;
  private final FetchLoanServiceFC fetchLoanServiceFC;

  @Override
  public ReportType getType() {
    return ReportType.LOAN_REPAYMENT;
  }

  @Override
  public LoanRepaymentReportDTO search(Long accountId, ReportFilterDTO reportFilterDTO) {
    log.info("Search(type={}, accountId={}, filters={}) is processing", getType(), accountId,
        reportFilterDTO);

    LoanRepaymentFilterDTO filters = (LoanRepaymentFilterDTO) reportFilterDTO;

    CustomerInfoDTO customer = fetchCustomerServiceFC.findByAccountIdMapped(accountId);
    AccountInfoDTO account = fetchAccountServiceFC.findAccountByAccountIdMapped(accountId);
    LoanInfoDTO loan = fetchLoanServiceFC.FetLoanByIdMapped(filters.getLoanId());
    List<LoanRepaymentInfoDTO> loanRepayments = fetchLoanServiceFC.fetchLoanRepaymentByLoanIdMapped(
        loan.getId());

    return LoanRepaymentReportDTO.builder()
        .customer(customer)
        .account(account)
        .loan(loan)
        .loanRepayments(loanRepayments).build();
  }
}
