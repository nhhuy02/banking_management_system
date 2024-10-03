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
    try {
      FetchResponseDTO<FetchAccountDataDTO> fetchResponseDTO = accountServiceFC.getAccountById(id);
      log.info("getAccountById({}): {}", id, fetchResponseDTO);

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
      log.error("Error fetch account from account service: {}", ex.getMessage());
      throw ex;
    }
  }

  public FetchAccountDataDTO getSavingsAccountById(long id) {
    try {
      log.info("getSavingsAccountById(id={}) is processing", id);
      FetchResponseDTO<FetchAccountDataDTO> fetchResponseDTO = accountServiceFC.getAccountById(id);
      log.info("getSavingsAccountById(id={}) passed: {}", id, fetchResponseDTO);

      return fetchResponseDTO.getData();
    } catch (Exception ex) {
      log.error("Error fetch saving-account with id={} from account service: {}", id,
          ex.getMessage());
      throw ex;
    }
  }
}
