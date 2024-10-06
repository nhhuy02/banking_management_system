

package com.ctv_it.klb.service;

import com.ctv_it.klb.config.exception.InvalidExceptionCustomize;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.dto.response.ErrorDetailDTO;
import com.ctv_it.klb.enumeration.ReportFormat;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.factory.ReportFormatServiceFactory;
import com.ctv_it.klb.factory.ReportTypeServiceFactory;
import com.ctv_it.klb.service.format.ReportFormatService;
import com.ctv_it.klb.service.type.ReportTypeService;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

  private final ReportTypeServiceFactory reportTypeServiceFactory;
  private final ReportFormatServiceFactory reportFormatServiceFactory;

  public Object report(Long accountId, ReportRequestDTO requestDTO) {
    ReportType reportType = requestDTO.getReportType();
    ReportFormat reportFormat = requestDTO.getReportFormat();
    ReportFilterDTO reportFilters = requestDTO.getReportFilters();

    // check support type
    ReportTypeService<?> reportTypeService = reportTypeServiceFactory.getReportTypeService(
        reportType);

    if (reportTypeService == null) {
      throw new InvalidExceptionCustomize(Collections.singletonList(
          ErrorDetailDTO.builder().field("reportType")
              .rejectedValue(reportType)
              .message("Report for type '" + reportType + "' is not supported")
              .build()));
    }

    // Return Object if request format NONE
    if (ReportFormat.NONE.equals(reportFormat)) {
      return search(accountId, reportFilters, reportTypeService);
    } else { // Else, check support format for type
      ReportFormatService<?> reportFormatService = reportFormatServiceFactory.getReportFormatService(
          reportType, reportFormat);

      if (reportFormatService == null) {
        throw new InvalidExceptionCustomize(
            Collections.singletonList(ErrorDetailDTO.builder().field("reportFormat")
                .rejectedValue(reportFormat).message(
                    "Report format '" + reportFormat + "' for type '"
                        + reportType + "' is not supported").build()));
      } else {
        return exportReport(
            search(accountId, reportFilters, reportTypeService),
            reportFormatService);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private <T> Object exportReport(Object reportData, ReportFormatService<T> reportFormatService) {
    return reportFormatService.export((T) reportData);
  }

  private Object search(Long accountId, ReportFilterDTO reportFilterDTO,
      ReportTypeService<?> reportTypeService) {
    return reportTypeService.search(accountId, reportFilterDTO);
  }
}
