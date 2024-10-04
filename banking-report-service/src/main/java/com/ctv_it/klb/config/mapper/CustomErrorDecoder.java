package com.ctv_it.klb.config.mapper;

import com.ctv_it.klb.config.exception.FetchErrorResponseExceptionCustomize;
import com.ctv_it.klb.dto.fetch.response.FetchResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomErrorDecoder implements ErrorDecoder {

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  public Exception decode(String methodKey, Response response) {
    log.info("Processing decode error response: {}", response.toString());
    return new FetchErrorResponseExceptionCustomize(parseResponse(response));
  }

  private FetchResponseDTO<?> parseResponse(Response response) {
    try {
      return objectMapper.readValue(response.body().asInputStream(), FetchResponseDTO.class);
    } catch (IOException e) {
      log.error("Failed to parse response body: {}", e.toString());
      throw new InternalError();
    }
  }
}
