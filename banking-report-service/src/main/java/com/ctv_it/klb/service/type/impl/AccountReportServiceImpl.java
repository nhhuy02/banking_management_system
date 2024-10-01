package com.ctv_it.klb.service.type.impl;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.AccountReportDTO;
import com.ctv_it.klb.dto.baseInfo.AccountInfoDTO;
import com.ctv_it.klb.dto.baseInfo.CustomerInfoDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchAccountDataDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchCustomerDataDTO;
import com.ctv_it.klb.dto.filter.extend.AccountFilterDTO;
import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.fetch.FetchAccountServiceFC;
import com.ctv_it.klb.service.fetch.FetchCustomerServiceFC;
import com.ctv_it.klb.service.type.ReportTypeService;
import java.util.ArrayList;
import java.util.List;
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
  public AccountReportDTO search(ReportRequestDTO reportRequestDTO) {
    log.info(Translator.toLocale("msg.called", "AccountReportServiceImpl::search"));

    FetchCustomerDataDTO fetchCustomerDataDTO = fetchCustomerServiceFC.findByAccountId(
        reportRequestDTO.getCustomerId());
    CustomerInfoDTO customerInfo = CustomerInfoDTO.builder()
        .fullName(fetchCustomerDataDTO.getFullName())
        .email(fetchCustomerDataDTO.getEmail())
        .phoneNumber(fetchCustomerDataDTO.getPhoneNumber())
        .address(fetchCustomerDataDTO.getCurrentAddress())
        .dateOfBirth(fetchCustomerDataDTO.getDateOfBirth())
        .kycDocumentType(fetchCustomerDataDTO.getKyc().getDocumentType())
        .kycDocumentNumber(fetchCustomerDataDTO.getKyc().getDocumentNumber())
        .build();
    log.info("Customer: {}", customerInfo);

    List<AccountInfoDTO> accountInfoDTOS = new ArrayList<>();

    AccountFilterDTO accountFilterDTO = (AccountFilterDTO) reportRequestDTO.getReportFilters();
    // get account info
    FetchAccountDataDTO fetchAccountDataDTO = fetchAccountServiceFC.getAccountById(
        accountFilterDTO.getAccountId());
    AccountInfoDTO accountInfo = AccountInfoDTO.builder()
        .id(1L)
        .number(fetchAccountDataDTO.getAccountNumber())
        .availableBalance(fetchAccountDataDTO.getBalance())
        .name(fetchAccountDataDTO.getAccountName())
        .type("Tài khoản thanh toán")
        .status(fetchAccountDataDTO.getStatus())
        .build();
    accountInfoDTOS.add(accountInfo);

    Long savingAccountId = accountFilterDTO.getSavingAccountId();
    if (savingAccountId != null) {
      FetchAccountDataDTO fetchSavingAccountDataDTO = fetchAccountServiceFC.getSavingsAccountById(
          savingAccountId);
      AccountInfoDTO savingAccountInfo = AccountInfoDTO.builder()
          .id(2L)
          .availableBalance(fetchSavingAccountDataDTO.getBalance())
          .name(fetchSavingAccountDataDTO.getAccountName())
          .type("Tài khoản tiết kiệm")
          .status(fetchSavingAccountDataDTO.getStatus())
          .build();
      accountInfoDTOS.add(savingAccountInfo);
    }
    log.info("AccountsInfo: {}", accountInfoDTOS);

    return AccountReportDTO.builder()
        .customer(customerInfo)
        .accounts(accountInfoDTOS)
        .build();
  }

  @Override
  public ReportType getType() {
    return ReportType.ACCOUNT;
  }
}
