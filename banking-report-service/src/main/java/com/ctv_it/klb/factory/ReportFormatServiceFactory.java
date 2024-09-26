package com.ctv_it.klb.factory;

import com.ctv_it.klb.config.i18n.Translator;
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
    this.reportServiceMap = reportFormatServices.stream().collect(Collectors.toMap(
        // Create a composite key for the map using AbstractMap.SimpleEntry
        service -> new AbstractMap.SimpleEntry<>(service.getType(), service.getFormat()),
        // Use the service itself as the value
        service -> service,
        // Merge function to handle any duplicate keys
        (existing, replacement) -> existing, // Keep the existing entry in case of duplicates
        // Initialize the map with a HashMap
        HashMap::new // You could use EnumMap or another map type if preferred
    ));
  }

  // Get the service by ReportType and ReportFormat using the composite key
  public ReportFormatService<?> getReportFormatService(ReportType type, ReportFormat format) {
    log.info(Translator.toLocale("msg.called",
        "ReportFormatServiceFactory::getReportFormatService: " + reportServiceMap.toString()));

    return reportServiceMap.get(new AbstractMap.SimpleEntry<>(type, format));
  }
}
