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
    log.info("Handle fetch response(fetchResponseDTO={}) is processing", fetchResponseDTO);

    if (fetchResponseDTO.isSuccess() && fetchResponseDTO.getData() != null) {
      log.info("Handle fetch response(fetchResponseDTO={}) completed successfully: \n{}",
          fetchResponseDTO, fetchResponseDTO.getData());

      return fetchResponseDTO.getData();
    } else {
      // Log the status and message when the response is not successful
      log.warn("Handle fetch response failed: \n{}", fetchResponseDTO);

      if (Objects.equals(HttpStatus.SC_BAD_REQUEST, fetchResponseDTO.getStatus())) {
        throw new InvalidExceptionCustomize(
            Collections.singletonList(
                ErrorDetailDTO.builder()
                    .message(fetchResponseDTO.getMessage())
                    .build()));
      } else {
        throw new FetchErrorResponseExceptionCustomize(fetchResponseDTO);
      }
    }
  }
}
