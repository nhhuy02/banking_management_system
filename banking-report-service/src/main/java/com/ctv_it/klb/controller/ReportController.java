package com.ctv_it.klb.controller;

import com.ctv_it.klb.config.exception.InvalidExceptionCustomize;
import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.dto.response.ErrorDetailDTO;
import com.ctv_it.klb.dto.response.SuccessResponseDTO;
import com.ctv_it.klb.factory.ReportServiceFactory;
import com.ctv_it.klb.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {

  private final ReportServiceFactory reportServiceFactory;

  @PostMapping("")
  public ResponseEntity<?> generateReport(HttpServletRequest request,
      @Valid @RequestBody ReportRequestDTO requestDTO) {

    ReportService<?> reportService = reportServiceFactory.getReportService(
        requestDTO.getReportType());
    if (reportService == null) {
      throw new InvalidExceptionCustomize(
          List.of(ErrorDetailDTO.builder()
              .field("reportType")
              .rejectedValue(requestDTO.getReportType())
              .message(Translator.toLocale("valid.enum.data-1",
                  reportServiceFactory.getReportServiceTypes()))
              .build()));
    }

    Object report = reportService.generate(requestDTO.getReportFilters());

    SuccessResponseDTO successResponseDTO = SuccessResponseDTO.builder()
        .status(Translator.toLocale("status.successfully"))
        .code(HttpStatus.OK.value())
        .url(request.getServletPath())
        .data(report)
        .build();

    return ResponseEntity.ok(successResponseDTO);
  }

}
