

package com.ctv_it.klb.service;

import com.ctv_it.klb.common.Default.File;
import com.ctv_it.klb.config.exception.InvalidExceptionCustomize;
import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.enumeration.ReportFormat;
import com.ctv_it.klb.factory.ReportFormatServiceFactory;
import com.ctv_it.klb.factory.ReportTypeServiceFactory;
import com.ctv_it.klb.service.format.ReportFormatService;
import com.ctv_it.klb.service.type.ReportTypeService;
import com.ctv_it.klb.util.FileUtil;
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
  private final FileUtil fileUtil;

  public Object report(ReportRequestDTO requestDTO) {
    // check support type
    ReportTypeService<?> reportTypeService = reportTypeServiceFactory.getReportTypeService(
        requestDTO.getReportType());

    if (reportTypeService == null) {
      throw new InvalidExceptionCustomize(Collections.singletonList(
          com.ctv_it.klb.dto.response.ErrorDetailDTO.builder().field("reportType")
              .rejectedValue(requestDTO.getReportType())
              .message("Report for type '" + requestDTO.getReportType() + "' is not supported")
              .build()));
    }

    // Return Object if request format NONE
    if (ReportFormat.NONE.equals(requestDTO.getReportFormat())) {
      return search(requestDTO, reportTypeService);
    } else { // Else, check support format for type
      ReportFormatService<?> reportFormatService = reportFormatServiceFactory.getReportFormatService(
          requestDTO.getReportType(), requestDTO.getReportFormat());

      if (reportFormatService == null) {
        throw new InvalidExceptionCustomize(Collections.singletonList(
            com.ctv_it.klb.dto.response.ErrorDetailDTO.builder().field("reportFormat")
                .rejectedValue(requestDTO.getReportFormat()).message(
                    "Report format '" + requestDTO.getReportFormat() + "' for type '"
                        + requestDTO.getReportType() + "' is not supported").build()));
      } else {
        String fileName = File.TARGET_PATH + "/" + generateFileName(requestDTO);
        return exportReport(fileName, search(requestDTO, reportTypeService), reportFormatService);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private <T> Object exportReport(String fileName, Object reportData,
      ReportFormatService<T> reportFormatService) {

    return reportFormatService.export(fileName, (T) reportData);
  }

  private Object search(ReportRequestDTO requestDTO, ReportTypeService<?> reportTypeService) {
    return reportTypeService.search(requestDTO);
  }

  private String generateFileName(ReportRequestDTO requestDTO) {
    return fileUtil.generateReportingFileName(requestDTO);
  }
}
