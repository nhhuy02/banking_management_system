package com.ctv_it.klb.service.impl;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.AccountReportDTO;
import com.ctv_it.klb.dto.base.AccountInfoDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchCustomerDataDTO;
import com.ctv_it.klb.dto.filter.extend.AccountFilterDTO;
import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.FetchCustomerServiceFC;
import com.ctv_it.klb.service.ReportService;
import com.ctv_it.klb.util.mock.MockFetchAccountService;
import com.ctv_it.klb.util.mock.MockFetchCustomerService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountReportServiceImpl implements ReportService<AccountReportDTO> {

  private final MockFetchCustomerService mockFetchCustomerService;
  private final MockFetchAccountService mockFetchAccountService;

  private final FetchCustomerServiceFC fetchCustomerServiceFC;

  @Override
  public AccountReportDTO report(ReportRequestDTO reportRequestDTO) {
    log.info(Translator.toLocale("CALLED", "AccountReportServiceImpl"));

    FetchCustomerDataDTO customer = getCustomerById(reportRequestDTO.getCustomerId());

    AccountFilterDTO accountFilterDTO = (AccountFilterDTO) reportRequestDTO.getReportFilters();
    log.info("accountFilterDTO: {}", accountFilterDTO);

    List<AccountInfoDTO> accounts = mockFetchAccountService.filter(reportRequestDTO.getCustomerId(),
        accountFilterDTO).getAccounts();

    return AccountReportDTO.builder()
        .customer(customer)
        .accounts(accounts)
        .build();
  }

  @Override
  public ReportType getReportType() {
    return ReportType.ACCOUNT;
  }

  private FetchCustomerDataDTO getCustomerById(long accountId) {
    return mockFetchCustomerService.findById(accountId);

//    return fetchCustomerServiceFC.findById(accountId);
  }

}
