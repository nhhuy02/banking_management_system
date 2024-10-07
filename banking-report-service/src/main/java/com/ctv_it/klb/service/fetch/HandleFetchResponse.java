package com.ctv_it.klb.service.fetch;

import com.ctv_it.klb.config.exception.FetchErrorResponseExceptionCustomize;
import com.ctv_it.klb.config.exception.InvalidExceptionCustomize;
import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.ctv_it.klb.dto.response.ErrorDetailDTO;
import java.util.Collections;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class HandleFetchResponse<T> {

  public T handle(FetchResponseDTO<T> fetchResponseDTO) {
    // Log the incoming fetch response
    log.info("Handling fetch response: {}", fetchResponseDTO);

    if (fetchResponseDTO.isSuccess() && fetchResponseDTO.getData() != null) {
      log.info("Fetch response successful with data: {}", fetchResponseDTO.getData());

      return fetchResponseDTO.getData();
    } else {
      // Log the status and message when the response is not successful
      log.warn("Fetch response not successful. Status: {}, Message: {}",
          fetchResponseDTO.getStatus(), fetchResponseDTO.getMessage());

      if (Objects.equals(HttpStatus.SC_BAD_REQUEST, fetchResponseDTO.getStatus())) {
        // Log specific details for bad requests
        log.warn("Invalid request: {}", fetchResponseDTO.getMessage());
        throw new InvalidExceptionCustomize(
            Collections.singletonList(
                ErrorDetailDTO.builder()
                    .message(fetchResponseDTO.getMessage())
                    .build()));
      } else {
        // Log error for fetch failures
        log.warn("Fetch error: Status {}, Message: {}", fetchResponseDTO.getStatus(),
            fetchResponseDTO.getMessage());
        throw new FetchErrorResponseExceptionCustomize(fetchResponseDTO);
      }
    }
  }
}
