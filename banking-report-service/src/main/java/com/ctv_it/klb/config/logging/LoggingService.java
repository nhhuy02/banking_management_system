package com.ctv_it.klb.config.logging;

import com.ctv_it.klb.util.GsonParserUtils;
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
    String sanitizedBody = GsonParserUtils.parseObjectToString(body);

    log.info("\nLOGGING REQUEST BODY-----------------------------------");
    log.info("[REQUEST-ID]: {}", requestId);
    log.info("[BODY REQUEST]: {}", sanitizedBody);
    log.info("LOGGING REQUEST BODY-----------------------------------\n");
  }

  public void logResponse(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, Object body) {
    if (httpServletRequest.getRequestURI().contains("medias")) {
      return;
    }
    Object requestId = httpServletRequest.getAttribute(REQUEST_ID);
    String sanitizedBody = GsonParserUtils.parseObjectToString(body);

    log.info("\nLOGGING RESPONSE-----------------------------------");
    log.info("[REQUEST-ID]: {}", requestId);
    log.info("[BODY RESPONSE]: {}", sanitizedBody);
    log.info("LOGGING RESPONSE-----------------------------------\n");
  }
}
