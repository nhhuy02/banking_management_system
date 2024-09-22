package com.ctv_it.klb.service.impl;

import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.enumeration.ReportFormat;
import com.ctv_it.klb.factory.ReportServiceFactory;
import com.ctv_it.klb.service.ReportFormatService;
import com.ctv_it.klb.service.ReportService;
import com.ctv_it.klb.service.ReportTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

  private final ReportServiceFactory reportServiceFactory;

  @Override
  public Object report(ReportRequestDTO request) {
    // Lấy ReportTypeService
    ReportTypeService<?> reportTypeService = reportServiceFactory.getReportTypeServiceFactory()
        .getReportTypeService(request.getReportType());

    if (reportTypeService == null) {
      log.warn("No report type service found for type: {}", request.getReportType());
      return null;
    }

    // Lấy dữ liệu báo cáo
    Object reportData = reportTypeService.search(request);

    if (ReportFormat.NONE.equals(request.getReportFormat())) {
      return reportData;
    } else {
      // Lấy ReportFormatService
      ReportFormatService<?> reportFormatService = reportServiceFactory.getReportFormatServiceFactory()
          .getReportFormatService(request.getReportFormat());

//      return exportReport(reportData, reportFormatService);
      return null;
    }
  }

  private <T> Object exportReport(Object reportData, ReportFormatService<T> reportFormatService) {
    return reportFormatService.export((T) reportData);
  }
}
