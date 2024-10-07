package com.ctv_it.klb.config.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoggingService {

  private static final String REQUEST_ID = "request_id";

  public void logRequest(HttpServletRequest httpServletRequest, Object body) {
    if (httpServletRequest.getRequestURI().contains("medias")) {
      return;
    }
    Object requestId = httpServletRequest.getAttribute(REQUEST_ID);
    String sanitizedBody = sanitizeBody(body);

    log.info("LOGGING REQUEST [{}]: {} : {}\n {}",
        requestId, httpServletRequest.getMethod(),
        httpServletRequest.getRequestURI(), sanitizedBody);
  }

  public void logResponse(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, Object body) {
    if (httpServletRequest.getRequestURI().contains("medias")) {
      return;
    }
    Object requestId = httpServletRequest.getAttribute(REQUEST_ID);
    String sanitizedBody = sanitizeBody(body);

    log.info("LOGGING RESPONSE [{}]: {} : {}\n {}",
        requestId, httpServletRequest.getMethod(),
        httpServletRequest.getRequestURI(), sanitizedBody);
  }

  private String sanitizeBody(Object body) {
    if (body instanceof byte[] byteArray) {
      // Convert the byte array to a hexadecimal string representation for clarity.
      StringBuilder hexString = new StringBuilder();
      for (int i = 0; i < Math.min(byteArray.length, 10); i++) {
        hexString.append(String.format("%02x, ", byteArray[i]));
      }
      return String.format("Binary content is truncated (size: %d bytes): {[%s...]}", byteArray.length, hexString.toString().trim());
    }
    return GsonParser.parseObjectToString(body);
  }


}
