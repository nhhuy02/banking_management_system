package com.ctv_it.klb.factory;

import com.ctv_it.klb.enumeration.ReportFormat;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.format.ReportFormatService;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReportFormatServiceFactory {

  private final Map<Map.Entry<ReportType, ReportFormat>, ReportFormatService<?>> reportServiceMap;

  @Autowired
  public ReportFormatServiceFactory(List<ReportFormatService<?>> reportFormatServices) {
    reportServiceMap = reportFormatServices.stream()
        .collect(
            Collectors.toMap(
                // Create a composite key for the map using AbstractMap.SimpleEntry
                service -> new AbstractMap.SimpleEntry<>(service.getType(), service.getFormat()),
                // Use the service itself as the value
                service -> service,
                // Merge function to handle any duplicate keys
                (existing, replacement) -> existing,
                HashMap::new
            ));
  }

  // Get the service by ReportType and ReportFormat using the composite key
  public ReportFormatService<?> getReportFormatService(ReportType type, ReportFormat format) {
    return reportServiceMap.get(new AbstractMap.SimpleEntry<>(type, format));
  }
}
