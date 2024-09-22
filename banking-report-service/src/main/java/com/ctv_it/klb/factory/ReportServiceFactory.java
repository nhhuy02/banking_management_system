package com.ctv_it.klb.factory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Getter
@RequiredArgsConstructor
public class ReportServiceFactory {

  private final ReportTypeServiceFactory reportTypeServiceFactory;
  private final ReportFormatServiceFactory reportFormatServiceFactory;
}
