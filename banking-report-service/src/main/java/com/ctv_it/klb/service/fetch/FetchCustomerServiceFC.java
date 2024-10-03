package com.ctv_it.klb.service.fetch;

import com.ctv_it.klb.config.exception.FetchErrorResponseExceptionCustomize;
import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchCustomerDataDTO;
import com.ctv_it.klb.feignClient.CustomerServiceFC;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FetchCustomerServiceFC {

  private final CustomerServiceFC customerServiceFC;

  public FetchCustomerDataDTO findByAccountId(long accountId) {
    try {
      log.info("findByAccountId(accountId = {}) is processing", accountId);
      FetchResponseDTO<FetchCustomerDataDTO> fetchResponseDTO = customerServiceFC.findByAccountId(
          accountId);
      log.info("findByAccountId(accountId = {}) passed: {}", accountId, fetchResponseDTO);

      if (fetchResponseDTO.isSuccess()) {
        return fetchResponseDTO.getData();
      } else {
        throw new FetchErrorResponseExceptionCustomize(fetchResponseDTO);
      }
    } catch (Exception ex) {
      log.error("findByAccountId(accountId = {}) by {}", accountId, ex.getMessage());
      throw ex;
    }
  }
}
