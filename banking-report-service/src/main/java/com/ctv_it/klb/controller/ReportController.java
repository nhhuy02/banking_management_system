package com.ctv_it.klb.controller;

import com.ctv_it.klb.config.i18n.Translator;
import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.dto.response.SuccessResponseDTO;
import com.ctv_it.klb.factory.ReportServiceFactory;
import com.ctv_it.klb.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
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
      @RequestBody ReportRequestDTO requestDTO) {

    ReportService<?> reportService = reportServiceFactory.getReportService(requestDTO.getType());

    Object report = reportService.generate(requestDTO.getFilters());

    return ResponseEntity.ok(SuccessResponseDTO.<Object>builder()
        .status(Translator.toLocale("status.successfully"))
        .code(HttpStatus.OK.value())
        .url(request.getServletPath())
        .data(report)
        .build());
  }
}
