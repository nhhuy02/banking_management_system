package com.ctv_it.klb.service.type.impl;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.AccountReportDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchCustomerDataDTO;
import com.ctv_it.klb.dto.filter.extend.AccountFilterDTO;
import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.fetch.FetchCustomerServiceFC;
import com.ctv_it.klb.service.type.ReportTypeService;
import com.ctv_it.klb.util.mock.MockFetchAccountService;
import com.ctv_it.klb.util.mock.MockFetchCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountReportServiceImpl implements ReportTypeService<AccountReportDTO> {

  private final MockFetchCustomerService mockFetchCustomerService;
  private final MockFetchAccountService mockFetchAccountService;

  private final FetchCustomerServiceFC fetchCustomerServiceFC;

  @Override
  public AccountReportDTO search(ReportRequestDTO reportRequestDTO) {
    log.info(Translator.toLocale("msg.called", "AccountReportServiceImpl::search"));

    FetchCustomerDataDTO customer = getCustomerById(reportRequestDTO.getCustomerId());

    AccountFilterDTO accountFilterDTO = (AccountFilterDTO) reportRequestDTO.getReportFilters();

    return null;
  }

  @Override
  public ReportType getType() {
    return ReportType.ACCOUNT;
  }

  private FetchCustomerDataDTO getCustomerById(long accountId) {
    return mockFetchCustomerService.findById(accountId);

//    return fetchCustomerServiceFC.findById(accountId);
  }
}
