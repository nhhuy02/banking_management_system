package com.ctv_it.klb.service;

import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.enumeration.ReportFormat;
import com.ctv_it.klb.factory.ReportFormatServiceFactory;
import com.ctv_it.klb.factory.ReportTypeServiceFactory;
import com.ctv_it.klb.service.format.ReportFormatService;
import com.ctv_it.klb.service.type.ReportTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

  private final ReportTypeServiceFactory reportTypeServiceFactory;
  private final ReportFormatServiceFactory reportFormatServiceFactory;

  public Object report(ReportRequestDTO request) {
    // Get ReportTypeService base on request.getReportType
    ReportTypeService<?> reportTypeService = reportTypeServiceFactory
        .getReportTypeService(request.getReportType());

    // Get report data
    Object reportData = reportTypeService.search(request);

    if (ReportFormat.NONE.equals(request.getReportFormat())) {
      return reportData;
    } else {
      // Get ReportFormatService base on request.getReportFormat
      ReportFormatService<?> reportFormatService = reportFormatServiceFactory
          .getReportFormatService(request.getReportType(), request.getReportFormat());

      return exportReport(reportData, reportFormatService);
//      return null;
    }
  }

  @SuppressWarnings("unchecked")
  private <T> Object exportReport(Object reportData, ReportFormatService<T> reportFormatService) {
    return reportFormatService.export((T) reportData);
  }
}
