package com.ctv_it.klb.service.type.impl;

import com.ctv_it.klb.dto.LoanReportDTO;
import com.ctv_it.klb.dto.baseInfo.AccountInfoDTO;
import com.ctv_it.klb.dto.baseInfo.CustomerInfoDTO;
import com.ctv_it.klb.dto.baseInfo.LoanInfoDTO;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import com.ctv_it.klb.dto.filter.extend.LoanFilterDTO;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.fetch.FetchAccountServiceFC;
import com.ctv_it.klb.service.fetch.FetchCustomerServiceFC;
import com.ctv_it.klb.service.fetch.FetchLoanServiceFC;
import com.ctv_it.klb.service.type.ReportTypeService;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanReportServiceImpl implements ReportTypeService<LoanReportDTO> {

  private final FetchAccountServiceFC fetchAccountServiceFC;
  private final FetchCustomerServiceFC fetchCustomerServiceFC;
  private final FetchLoanServiceFC fetchLoanServiceFC;

  @Override
  public ReportType getType() {
    return ReportType.LOAN;
  }

  @Override
  public LoanReportDTO search(Long accountId, ReportFilterDTO reportFilterDTO) {
    log.info("Search(type={}, accountId={}, filters={}) is processing",
        getType(), accountId, reportFilterDTO);

    LoanFilterDTO filters = (LoanFilterDTO) reportFilterDTO;

    CustomerInfoDTO customer = fetchCustomerServiceFC.fetchCustomerByAccountId(accountId);
    AccountInfoDTO account = fetchAccountServiceFC.fetchAccountById(accountId);
    List<LoanInfoDTO> loans = filters(accountId, filters);

    return LoanReportDTO.builder()
        .customer(customer)
        .account(account)
        .loans(loans)
        .build();
  }

  private List<LoanInfoDTO> filters(long accountId, LoanFilterDTO loanFilterDTO) {

    Long loanTypeId = null;
    LocalDate loanRepaymentScheduleFrom = null;
    LocalDate loanRepaymentScheduleTo = null;
    Set<String> loanStatus = null;

    if (loanFilterDTO != null) {
      loanTypeId = loanFilterDTO.getLoanTypeId();
      if (loanFilterDTO.getLoanRepaymentScheduleRange() != null) {
        loanRepaymentScheduleFrom = loanFilterDTO.getLoanRepaymentScheduleRange().getMin();
        loanRepaymentScheduleTo = loanFilterDTO.getLoanRepaymentScheduleRange().getMax();
      }
      loanStatus = loanFilterDTO.getLoanStatus();
    }

    return fetchLoanServiceFC.fetchLoanByAccountId(
        accountId,
        loanTypeId,
        loanRepaymentScheduleFrom,
        loanRepaymentScheduleTo,
        loanStatus);
  }
}
