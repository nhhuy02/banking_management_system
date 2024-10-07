package com.ctv_it.klb.service.type.impl;

import com.ctv_it.klb.dto.AccountReportDTO;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import com.ctv_it.klb.dto.filter.extend.AccountFilterDTO;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.fetch.FetchAccountServiceFC;
import com.ctv_it.klb.service.fetch.FetchCustomerServiceFC;
import com.ctv_it.klb.service.type.ReportTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountReportServiceImpl implements ReportTypeService<AccountReportDTO> {

  private final FetchCustomerServiceFC fetchCustomerServiceFC;
  private final FetchAccountServiceFC fetchAccountServiceFC;

  @Override
  public ReportType getType() {
    return ReportType.ACCOUNT;
  }

  @Override
  public AccountReportDTO search(Long accountId, ReportFilterDTO reportFilterDTO) {
    log.info("Search(type={}, accountId={}, filters={}) is processing",
        getType(), accountId, reportFilterDTO);

    AccountFilterDTO accountFilters = (AccountFilterDTO) reportFilterDTO;

    return AccountReportDTO.builder()
        .customer(fetchCustomerServiceFC.fetchCustomerByAccountId(accountId))
        .accounts(fetchAccountServiceFC.fetchAccounts(accountId, accountFilters))
        .build();
  }
}
