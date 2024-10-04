package com.ctv_it.klb.service.type.impl;

import com.ctv_it.klb.dto.AccountReportDTO;
import com.ctv_it.klb.dto.baseInfo.AccountInfoDTO;
import com.ctv_it.klb.dto.baseInfo.CustomerInfoDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchAccountDataDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchCustomerDataDTO;
import com.ctv_it.klb.dto.filter.extend.AccountFilterDTO;
import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.enumeration.BaseStatus;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.fetch.FetchAccountServiceFC;
import com.ctv_it.klb.service.fetch.FetchCustomerServiceFC;
import com.ctv_it.klb.service.type.ReportTypeService;
import java.time.format.DateTimeFormatter;
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
  public AccountReportDTO search(Long accountId, ReportRequestDTO reportRequestDTO) {
    log.info("AccountReportServiceImpl::search started");

    AccountFilterDTO accountFilterDTO = (AccountFilterDTO) reportRequestDTO.getReportFilters();
    log.info("Received valid accountFilterDTO: {}", accountFilterDTO);

    AccountReportDTO reportDTO = AccountReportDTO.builder()
        .customer(this.fetchCustomerByAccountId(accountId))
        .accounts(this.fetchAccounts(accountId, accountFilterDTO.getSavingAccountId()))
        .build();

    log.info("AccountReportDTO successfully built: {}", reportDTO);
    return reportDTO;
  }

  @Override
  public ReportType getType() {
    return ReportType.ACCOUNT;
  }

  private CustomerInfoDTO fetchCustomerByAccountId(long accountId) {
    log.info("Fetching customer data for accountId: {}", accountId);
    FetchCustomerDataDTO data = fetchCustomerServiceFC.findByAccountId(accountId);
    CustomerInfoDTO customer = this.map(data);
    log.info("Mapped customer data: {}", customer);
    return customer;
  }

  private List<AccountInfoDTO> fetchAccounts(Long accountId, Long savingAccountId) {
    log.info("Fetching accounts for accountId: {}, savingAccountId: {}", accountId,
        savingAccountId);

    List<AccountInfoDTO> accounts = new ArrayList<>();
    accounts.add(this.fetchAccountById(accountId));

    if (savingAccountId != null) {
      log.info("Fetching saving account for savingAccountId: {}", savingAccountId);
      accounts.add(this.fetchSavingAccountById(savingAccountId));
    }

    log.info("Mapped accounts: {}", accounts);
    return accounts;
  }

  private AccountInfoDTO fetchAccountById(long accountId) {
    log.info("Fetching account by accountId: {}", accountId);
    FetchAccountDataDTO data = fetchAccountServiceFC.getAccountById(accountId);
    AccountInfoDTO account = map(data);
    account.setNo(1L);
    account.setType("Tài khoản thanh toán");
    log.info("Mapped account data: {}", account);
    return account;
  }

  private AccountInfoDTO fetchSavingAccountById(long savingAccountId) {
    log.info("Fetching saving account by savingAccountId: {}", savingAccountId);

    FetchAccountDataDTO data = fetchAccountServiceFC.getSavingsAccountById(savingAccountId);
    AccountInfoDTO savingAccount = map(data);
    savingAccount.setNo(2L);
    savingAccount.setType("Tài khoản tiết kiệm");
    log.info("Mapped saving account data: {}", savingAccount);
    return savingAccount;
  }

  private AccountInfoDTO map(FetchAccountDataDTO fetchAccountDataDTO) {
    log.debug("Mapping FetchAccountDataDTO to AccountInfoDTO: {}", fetchAccountDataDTO);
    return AccountInfoDTO.builder()
        .id(fetchAccountDataDTO.getId())
        .number(fetchAccountDataDTO.getAccountNumber())
        .availableBalance(fetchAccountDataDTO.getBalance())
        .name(fetchAccountDataDTO.getAccountName())
        .status(BaseStatus.valueOf(fetchAccountDataDTO.getStatus().toUpperCase()).getValue())
        .build();
  }

  private CustomerInfoDTO map(FetchCustomerDataDTO fetchCustomerDataDTO) {
    log.debug("Mapping FetchCustomerDataDTO to CustomerInfoDTO: {}", fetchCustomerDataDTO);
    return CustomerInfoDTO.builder()
        .fullName(fetchCustomerDataDTO.getFullName())
        .email(fetchCustomerDataDTO.getEmail())
        .phoneNumber(fetchCustomerDataDTO.getPhoneNumber())
        .address(fetchCustomerDataDTO.getCurrentAddress())
        .dateOfBirth(fetchCustomerDataDTO.getDateOfBirth().format(
            DateTimeFormatter.ofPattern("dd-MM-yyyy")))
        .kycDocumentType(fetchCustomerDataDTO.getKyc().getDocumentType())
        .kycDocumentNumber(fetchCustomerDataDTO.getKyc().getDocumentNumber())
        .build();
  }
}
