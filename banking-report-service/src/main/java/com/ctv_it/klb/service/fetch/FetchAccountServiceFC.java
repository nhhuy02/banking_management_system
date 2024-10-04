package com.ctv_it.klb.service.fetch;

import com.ctv_it.klb.config.exception.InvalidExceptionCustomize;
import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.fetch.response.data.FetchAccountDataDTO;
import com.ctv_it.klb.dto.response.ErrorDetailDTO;
import com.ctv_it.klb.feignClient.AccountServiceFC;
import java.util.Collections;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FetchAccountServiceFC {

  private final AccountServiceFC accountServiceFC;

  public FetchAccountDataDTO getAccountById(long id) {
    FetchResponseDTO<FetchAccountDataDTO> fetchResponseDTO = accountServiceFC.getAccountById(id);
    log.info("getAccountById({}): {}", id, fetchResponseDTO);
    return handleFetchResponse(fetchResponseDTO);
  }

  public FetchAccountDataDTO getSavingsAccountById(long id) {
    FetchResponseDTO<FetchAccountDataDTO> fetchResponseDTO = accountServiceFC.getSavingsAccountById(
        id);
    log.info("getSavingsAccountById({}): {}", id, fetchResponseDTO);
    return handleFetchResponse(fetchResponseDTO);
  }

  private FetchAccountDataDTO handleFetchResponse(
      FetchResponseDTO<FetchAccountDataDTO> fetchResponseDTO) {
    if (fetchResponseDTO.isSuccess() && fetchResponseDTO.getData() != null) {
      log.info("Fetch successful: {}", fetchResponseDTO.getData());
      return fetchResponseDTO.getData();
    } else {
      log.warn("Fetch failed with status: {}, message: {}",
          fetchResponseDTO.getStatus(), fetchResponseDTO.getMessage());

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
  }
}
