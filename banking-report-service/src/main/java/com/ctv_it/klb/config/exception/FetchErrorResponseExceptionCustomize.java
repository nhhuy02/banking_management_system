package com.ctv_it.klb.config.exception;

import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FetchErrorResponseExceptionCustomize extends RuntimeException {

  private FetchResponseDTO<?> fetchResponseDTO;

  public FetchErrorResponseExceptionCustomize(FetchResponseDTO<?> fetchResponseDTO) {
    super(fetchResponseDTO.getMessage());
    this.fetchResponseDTO = fetchResponseDTO;
  }
}
