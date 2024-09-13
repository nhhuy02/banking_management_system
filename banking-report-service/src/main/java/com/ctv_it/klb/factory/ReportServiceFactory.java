package com.ctv_it.klb.factory;

import com.ctv_it.klb.service.ReportService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportServiceFactory {

  private final Map<String, ReportService<?>> reportServiceMap;

  @Autowired
  public ReportServiceFactory(List<ReportService<?>> reportServices) {
    this.reportServiceMap = reportServices.stream()
        .collect(Collectors.toMap(
            service -> service.getReportType().name(),
            service -> service));
  }

  public ReportService<?> getReportService(String reportType) {
    return reportServiceMap.get(reportType);
  }
}
