package com.ctv_it.klb.service.fetch;

import com.ctv_it.klb.dto.baseInfo.AccountInfoDTO;
import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchAccountDataResponseDTO;
import com.ctv_it.klb.dto.filter.extend.AccountFilterDTO;
import com.ctv_it.klb.enumeration.BaseStatus;
import com.ctv_it.klb.feignClient.AccountServiceFC;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FetchAccountServiceFC {

  private final AccountServiceFC accountServiceFC;
  private final HandleFetchResponse<FetchAccountDataResponseDTO> handleFetchResponse;

  public FetchAccountDataResponseDTO getAccountById(long accountId) {
    try {
      log.info("Fetch account(accountId={}) is processing", accountId);
      FetchResponseDTO<FetchAccountDataResponseDTO> fetchResponseDTO = accountServiceFC.getAccountById(
          accountId);
      log.info("Fetch account(accountId={}) passed: {}", accountId, fetchResponseDTO);

      return handleFetchResponse.handle(fetchResponseDTO);
    } catch (Exception ex) {
      log.error("Fetch account(accountId={}) failed: {}", accountId, ex.toString());
      throw ex;
    }
  }

  public FetchAccountDataResponseDTO getSavingsAccountById(long savingAccountId) {
    try {
      log.info("Fetch savingAccount(savingAccountId={}) is processing", savingAccountId);
      FetchResponseDTO<FetchAccountDataResponseDTO> fetchResponseDTO = accountServiceFC.getSavingsAccountById(
          savingAccountId);
      log.info("Fetch account(savingAccountId={}) passed: {}", savingAccountId, fetchResponseDTO);

      return handleFetchResponse.handle(fetchResponseDTO);
    } catch (Exception ex) {
      log.error("Fetch account(savingAccountId={}) failed: {}", savingAccountId, ex.toString());
      throw ex;
    }
  }

  public AccountInfoDTO map(FetchAccountDataResponseDTO fetchDataResponseDTO) {
    log.debug("Mapping FetchAccountDataDTO to AccountInfoDTO: {}", fetchDataResponseDTO);
    return AccountInfoDTO.builder()
        .id(fetchDataResponseDTO.getAccountId())
        .number(fetchDataResponseDTO.getAccountNumber())
        .availableBalance(fetchDataResponseDTO.getBalance())
        .name(fetchDataResponseDTO.getAccountName())
        .status(
            BaseStatus.valueOf(fetchDataResponseDTO.getStatus().toUpperCase()).getValue())
        .build();
  }

  public List<AccountInfoDTO> fetchAccounts(Long accountId, AccountFilterDTO accountFilterDTO) {
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

  public AccountInfoDTO fetchAccountById(long accountId) {
    log.info("Fetching account(accountId={})", accountId);
    FetchAccountDataResponseDTO data = getAccountById(accountId);
    AccountInfoDTO account = map(data);
    account.setNo(1L);
    account.setType("Tài khoản thanh toán");
    log.info("Mapped account data: {}", account);
    return account;
  }

  public AccountInfoDTO fetchSavingAccountById(long savingAccountId) {
    log.info("Fetching saving account by savingAccountId: {}", savingAccountId);

    FetchAccountDataResponseDTO data = getSavingsAccountById(savingAccountId);
    AccountInfoDTO savingAccount = map(data);
    savingAccount.setNo(2L);
    savingAccount.setType("Tài khoản tiết kiệm");
    log.info("Mapped saving account data: {}", savingAccount);
    return savingAccount;
  }
}
