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
    String data =
        "\nLOGGING REQUEST BODY-----------------------------------\n" + "[REQUEST-ID]: " + requestId
            + "\n" + "[BODY REQUEST]: " + "\n\n" + sanitizedBody + "\n\n"
            + "LOGGING REQUEST BODY-----------------------------------\n";

    log.info(data);
  }

  public void logResponse(HttpServletRequest httpServletRequest,
      HttpServletResponse httpServletResponse, Object body) {
    if (httpServletRequest.getRequestURI().contains("medias")) {
      return;
    }
    Object requestId = httpServletRequest.getAttribute(REQUEST_ID);
    String sanitizedBody = GsonParserUtils.parseObjectToString(body);
    String data =
        "\nLOGGING RESPONSE-----------------------------------\n" + "[REQUEST-ID]: " + requestId
            + "\n" + "[BODY RESPONSE]: " + "\n\n" + sanitizedBody + "\n\n"
            + "LOGGING RESPONSE-----------------------------------\n";

    log.info(data);
  }
}
