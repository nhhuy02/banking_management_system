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
      FetchResponseDTO<FetchCustomerDataDTO> fetchResponseDTO = customerServiceFC.findByAccountId(
          accountId);
      log.info("findByAccountId({}): {}", accountId, fetchResponseDTO);

      if (fetchResponseDTO.isSuccess()) {
        return fetchResponseDTO.getData();
      } else {
        throw new FetchErrorResponseExceptionCustomize(fetchResponseDTO);
      }
    } catch (Exception ex) {
      log.error("Error fetch customer service: {}", ex.getMessage());
      throw ex;
    }
  }
}
