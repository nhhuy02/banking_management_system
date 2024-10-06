package com.ctv_it.klb.service.type.impl;

import com.ctv_it.klb.dto.AccountReportDTO;
import com.ctv_it.klb.dto.baseInfo.AccountInfoDTO;
import com.ctv_it.klb.dto.baseInfo.CustomerInfoDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchAccountDataResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchCustomerDataResponseDTO;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import com.ctv_it.klb.dto.filter.extend.AccountFilterDTO;
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
  public ReportType getType() {
    return ReportType.ACCOUNT;
  }

  @Override
  public AccountReportDTO search(Long accountId, ReportFilterDTO reportFilterDTO) {
    log.info(
        "AccountReportServiceImpl::search(accountId={}, reportFilterDTO={}) is processing",
        accountId, reportFilterDTO);

    AccountFilterDTO accountFilters = (AccountFilterDTO) reportFilterDTO;

    AccountReportDTO reportDTO = AccountReportDTO.builder()
        .customer(fetchCustomerByAccountId(accountId))
        .accounts(fetchAccounts(accountId, accountFilters))
        .build();

    log.info("AccountReportDTO successfully built: {}", reportDTO);
    return reportDTO;
  }

  private CustomerInfoDTO fetchCustomerByAccountId(long accountId) {
    log.info("Fetching customer data for accountId: {}", accountId);
    FetchCustomerDataResponseDTO data = fetchCustomerServiceFC.findByAccountId(accountId);
    CustomerInfoDTO customer = fetchCustomerServiceFC.map(data);
    log.info("Mapped customer data: {}", customer);
    return customer;
  }

  private List<AccountInfoDTO> fetchAccounts(Long accountId, AccountFilterDTO accountFilterDTO) {
    log.info("Fetching accounts(accountId: {}, accountFilterDTO: {})", accountId,
        accountFilterDTO);

    List<AccountInfoDTO> accounts = new ArrayList<>();
    accounts.add(fetchAccountById(accountId));

    if (accountFilterDTO != null && accountFilterDTO.getSavingAccountId() != null) {
      log.info("Fetching savingAccountId(savingAccountId={})",
          accountFilterDTO.getSavingAccountId());
      accounts.add(fetchSavingAccountById(accountFilterDTO.getSavingAccountId()));
    }

    log.info("Mapped accounts: {}", accounts);
    return accounts;
  }

  private AccountInfoDTO fetchAccountById(long accountId) {
    log.info("Fetching account(accountId={})", accountId);
    FetchAccountDataResponseDTO data = fetchAccountServiceFC.getAccountById(accountId);
    AccountInfoDTO account = fetchAccountServiceFC.map(data);
    account.setNo(1L);
    account.setType("Tài khoản thanh toán");
    log.info("Mapped account data: {}", account);
    return account;
  }

  private AccountInfoDTO fetchSavingAccountById(long savingAccountId) {
    log.info("Fetching saving account by savingAccountId: {}", savingAccountId);

    FetchAccountDataResponseDTO data = fetchAccountServiceFC.getSavingsAccountById(savingAccountId);
    AccountInfoDTO savingAccount = fetchAccountServiceFC.map(data);
    savingAccount.setNo(2L);
    savingAccount.setType("Tài khoản tiết kiệm");
    log.info("Mapped saving account data: {}", savingAccount);
    return savingAccount;
  }
}
