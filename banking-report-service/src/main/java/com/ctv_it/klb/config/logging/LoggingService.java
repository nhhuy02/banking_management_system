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
    String sanitizedBody = GsonParser.parseObjectToString(body);

    log.info("\nLOGGING REQUEST [{}]:",requestId);
    log.info("\n\t[BODY REQUEST]: {}\n", sanitizedBody);
  }

  public void logResponse(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, Object body) {
    if (httpServletRequest.getRequestURI().contains("medias")) {
      return;
    }
    Object requestId = httpServletRequest.getAttribute(REQUEST_ID);
    String sanitizedBody = GsonParser.parseObjectToString(body);

    log.info("\nLOGGING RESPONSE [{}]:",requestId);
    log.info("\n\t[BODY RESPONSE]: {}\n", sanitizedBody);
  }
}
