package com.ctv_it.klb.config.logging;


import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomURLFilter implements Filter {

  private static final String REQUEST_ID = "request_id";

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException {

    Long requestId = System.currentTimeMillis();
    servletRequest.setAttribute(REQUEST_ID, requestId);
    logRequest((HttpServletRequest) servletRequest, requestId);
    filterChain.doFilter(servletRequest, servletResponse);
  }

  private void logRequest(HttpServletRequest request, Object requestId) {
    if (request != null) {
      StringBuilder data = new StringBuilder();
      data.append("\nLOGGING REQUEST-----------------------------------\n").append("[REQUEST-ID]: ")
          .append(requestId).append("\n").append("[PATH]: ").append(request.getRequestURI())
          .append("\n").append("[QUERIES]: ").append(request.getQueryString()).append("\n")
          .append("[HEADERS]: ").append("\n");

      Enumeration<String> headerNames = request.getHeaderNames();
      while (headerNames.hasMoreElements()) {
        String key = headerNames.nextElement();
        String value = request.getHeader(key);
        data.append("---").append(key).append(" : ").append(value).append("\n");
      }
      data.append("LOGGING REQUEST-----------------------------------\n");

      log.info(data.toString());
    }
  }
}
