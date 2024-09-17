package com.ctv_it.klb.controller;

import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.dto.response.ErrorResponseDTO;
import com.ctv_it.klb.dto.response.SuccessResponseDTO;
import com.ctv_it.klb.factory.ReportServiceFactory;
import com.ctv_it.klb.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "Report", description = "Endpoints for generate and export report")
public class ReportController {

  private final ReportServiceFactory reportServiceFactory;


  @Operation(summary = "Generate report", description = "Generates a report based on the report type specified.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200",
          description = """
               Successfully generated the report.
               The response data will be one of the following based on the report type:
                - `ACCOUNT` -> AccountReportDTO
                - `LOAN` -> LoanReportDTO
                - `TRANSACTION` -> TransactionReportDTO
              """,
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = SuccessResponseDTO.class)
          )
      ), @ApiResponse(responseCode = "400", description = "Bad request",
      content = @Content(
          mediaType = "application/json",
          schema = @Schema(implementation = ErrorResponseDTO.class)
      )
  ),
      @ApiResponse(responseCode = "500", description = "Internal server error",
          content = @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = ErrorResponseDTO.class)
          )
      )
  })
  @PostMapping("/generate")
  public ResponseEntity<?> generate(HttpServletRequest request,
      @Valid @RequestBody ReportRequestDTO reportRequestDTO) {
    log.info("Received ReportRequestDTO: {}", reportRequestDTO);

    ReportService<?> reportService = reportServiceFactory.getReportService(
        reportRequestDTO.getReportType());

    if (reportService == null) {
      log.error("ReportServiceFactory not found reportServiceImpl for reportType with value '{}'",
          reportRequestDTO.getReportType());
      throw new InternalError("");
    }

    Object report = reportService.generate(reportRequestDTO);

    SuccessResponseDTO successResponseDTO = SuccessResponseDTO.builder()
        .url(request.getServletPath())
        .data(report)
        .build();

    return ResponseEntity.ok(successResponseDTO);
  }

}
