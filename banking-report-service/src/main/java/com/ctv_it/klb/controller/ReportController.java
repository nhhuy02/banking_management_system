package com.ctv_it.klb.controller;

import com.ctv_it.klb.dto.request.ReportRequestDTO;
import com.ctv_it.klb.dto.response.ErrorResponseDTO;
import com.ctv_it.klb.dto.response.SuccessResponseDTO;
import com.ctv_it.klb.enumeration.ReportFormat;
import com.ctv_it.klb.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
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

  private final ReportService reportService;

  @Operation(summary = "Generate report", description = "Generates a report based on the report type specified.")
  @ApiResponses(value = {@ApiResponse(responseCode = "200", description = """
        Successfully generated the report.
        The response data will be one of the following based on the reportType and reportFormat:
        - reportType:
            + `ACCOUNT` -> AccountReportDTO
            + `LOAN` -> LoanReportDTO
            + `TRANSACTION` -> TransactionReportDTO
        - reportFormat:
            + `NONE` -> `application/json` for JSON
            + `PDF` -> `application/pdf` for PDF file
            + `EXCEL` -> `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet` for Excel file
      """, content = @Content(mediaType = "application/json", schema = @Schema(implementation = SuccessResponseDTO.class))),
      @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
      @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))})
  @PostMapping("")
  public ResponseEntity<?> report(HttpServletRequest request,
      @RequestBody ReportRequestDTO reportRequestDTO) {

    log.info("Received ReportRequestDTO: {}", reportRequestDTO);

    Object reportData = reportService.report(reportRequestDTO);
    SuccessResponseDTO response = SuccessResponseDTO.builder().url(request.getServletPath())
        .data(reportData).build();

    ResponseEntity.BodyBuilder responseBuilder = ResponseEntity.ok();
//    setResponseHeaders(responseBuilder, reportRequestDTO.getReportFormat());

    return responseBuilder.body(response);
  }

  private void setResponseHeaders(ResponseEntity.BodyBuilder responseBuilder,
      ReportFormat reportFormat) {
    if (ReportFormat.NONE.equals(reportFormat)) {
      responseBuilder.header("Content-Type", "application/json");
    } else if (ReportFormat.PDF.equals(reportFormat)) {
      responseBuilder.header("Content-Type", "application/pdf");
    } else if (ReportFormat.EXCEL.equals(reportFormat)) {
      responseBuilder.header("Content-Type",
          "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
  }
}
