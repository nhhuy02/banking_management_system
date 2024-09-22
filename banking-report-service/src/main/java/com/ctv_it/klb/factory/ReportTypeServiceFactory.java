package com.ctv_it.klb.factory;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.ReportTypeService;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReportTypeServiceFactory {

  private final Map<ReportType, ReportTypeService<?>> reportServiceMap = new EnumMap<>(
      ReportType.class);

  @Autowired
  public ReportTypeServiceFactory(List<ReportTypeService<?>> reportTypeServices) {
    reportTypeServices.forEach(service -> reportServiceMap.put(service.getType(), service));
    log.info("ReportFormatServiceFactory {}", reportServiceMap);
  }

  public ReportTypeService<?> getReportTypeService(ReportType reportType) {
    log.info(Translator.toLocale("msg.called", "ReportTypeServiceFactory::getReportTypeService"));
    return reportServiceMap.get(reportType);
  }
}
