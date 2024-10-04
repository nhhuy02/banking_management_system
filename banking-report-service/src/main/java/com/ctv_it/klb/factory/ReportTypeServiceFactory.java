package com.ctv_it.klb.factory;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.service.type.ReportTypeService;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReportTypeServiceFactory {

  private final Map<ReportType, ReportTypeService<?>> reportTypeServiceMap = new EnumMap<>(
      ReportType.class);

  @Autowired
  public ReportTypeServiceFactory(List<ReportTypeService<?>> reportTypeServices) {
    reportTypeServices.forEach(service -> reportTypeServiceMap.put(service.getType(), service));
  }

  public ReportTypeService<?> getReportTypeService(ReportType reportType) {
    log.info("ReportTypeServiceFactory::getReportTypeService is processing for type: {}", reportType);

    return reportTypeServiceMap.get(reportType);
  }
}
