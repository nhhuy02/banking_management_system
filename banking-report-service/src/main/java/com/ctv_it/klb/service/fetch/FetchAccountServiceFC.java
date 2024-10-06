package com.ctv_it.klb.service.fetch;

import com.ctv_it.klb.dto.baseInfo.AccountInfoDTO;
import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchAccountDataResponseDTO;
import com.ctv_it.klb.enumeration.BaseStatus;
import com.ctv_it.klb.feignClient.AccountServiceFC;
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
}
