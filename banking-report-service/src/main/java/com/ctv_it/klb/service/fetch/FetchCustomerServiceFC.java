package com.ctv_it.klb.service.fetch;

import com.ctv_it.klb.config.exception.InvalidExceptionCustomize;
import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchCustomerDataDTO;
import com.ctv_it.klb.dto.response.ErrorDetailDTO;
import com.ctv_it.klb.feignClient.CustomerServiceFC;
import java.util.Collections;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FetchCustomerServiceFC {

  private final CustomerServiceFC customerServiceFC;

  public FetchCustomerDataDTO findById(long accountId) {
    try {
      FetchResponseDTO<FetchCustomerDataDTO> fetchResponseDTO = customerServiceFC.findById(
          accountId);

      if (fetchResponseDTO.isSuccess() && fetchResponseDTO.getData() != null) {
        return fetchResponseDTO.getData();
      } else {
        if (Objects.equals(HttpStatus.SC_BAD_REQUEST, fetchResponseDTO.getStatus())) {
          throw new InvalidExceptionCustomize(
              Collections.singletonList(
                  ErrorDetailDTO.builder()
                      .message(fetchResponseDTO.getMessage())
                      .build()));

        } else {
          throw new InternalError(fetchResponseDTO.getMessage());
        }
      }
    } catch (Exception ex) {
      log.error("Error fetch customer service: {}", ex.getMessage());
      throw ex;
    }
  }
}
