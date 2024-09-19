package com.ctv_it.klb.service.impl;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.AccountReportDTO;
import com.ctv_it.klb.dto.base.AccountInfoDTO;
import com.ctv_it.klb.dto.fetch.response.FetchCustomerDataDTO;
import com.ctv_it.klb.dto.filter.extend.AccountFilterDTO;
import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.util.FakeMockServices.AccountFetchService;
import com.ctv_it.klb.util.FakeMockServices.CustomerFetchService;
import com.ctv_it.klb.service.ReportService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountReportServiceImpl implements ReportService<AccountReportDTO> {

  private final CustomerFetchService customerFetchService;
  private final AccountFetchService accountFetchService;

  @Override
  public AccountReportDTO report(ReportRequestDTO reportRequestDTO) {
    log.info(Translator.toLocale("CALLED", "AccountReportServiceImpl"));

    // Find customer by ID
    FetchCustomerDataDTO customer = customerFetchService.findById(
        reportRequestDTO.getCustomerId());

    AccountFilterDTO accountFilterDTO = (AccountFilterDTO) reportRequestDTO.getReportFilters();
    log.info("accountFilterDTO: {}", accountFilterDTO);

    List<AccountInfoDTO> accounts = accountFetchService.filter(reportRequestDTO.getCustomerId(),
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

}
