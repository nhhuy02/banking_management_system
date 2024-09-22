package com.ctv_it.klb.factory;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.enumeration.ReportFormat;
import com.ctv_it.klb.service.ReportFormatService;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReportFormatServiceFactory {

  private final Map<ReportFormat, ReportFormatService<?>> reportFormatServiceMap = new EnumMap<>(
      ReportFormat.class);

  @Autowired
  public ReportFormatServiceFactory(List<ReportFormatService<?>> reportFormatServices) {
    reportFormatServices.forEach(
        service -> reportFormatServiceMap.put(service.getFormat(), service));
    log.info("ReportFormatServiceFactory {}", reportFormatServiceMap);
  }

  public ReportFormatService<?> getReportFormatService(ReportFormat format) {
    log.info(
        Translator.toLocale("msg.called", "ReportFormatServiceFactory::getReportFormatService"));
    return reportFormatServiceMap.get(format);
  }
}
