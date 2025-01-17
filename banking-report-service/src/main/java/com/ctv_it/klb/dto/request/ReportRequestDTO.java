package com.ctv_it.klb.dto.request;

import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import com.ctv_it.klb.dto.filter.extend.AccountFilterDTO;
import com.ctv_it.klb.dto.filter.extend.LoanFilterDTO;
import com.ctv_it.klb.dto.filter.extend.LoanRepaymentFilterDTO;
import com.ctv_it.klb.dto.filter.extend.TransactionFilterDTO;
import com.ctv_it.klb.enumeration.ReportFormat;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.util.ReportRequestDTODeserializerUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
@JsonDeserialize(using = ReportRequestDTODeserializerUtil.class)
public class ReportRequestDTO {

  @NotNull
  @Schema(description = "Type of reporting", implementation = ReportType.class)
  private ReportType reportType;

  @NotNull
  @Schema(description = "Format of reporting export", implementation = ReportFormat.class)
  private ReportFormat reportFormat;

  @Schema(
      description = """
              Filters applied based on the report type:
              - `ACCOUNT` -> `reportFilters` : AccountFilterDTO.class
              - `LOAN` -> `reportFilters` : LoanFilterDTO.class
              - `LOAN_REPAYMENT` -> `reportFilters` : LoanRepaymentFilterDTO.class
              - `TRANSACTION` -> `reportFilters` : TransactionFilterDTO.class
          """,
      oneOf = {
          AccountFilterDTO.class,
          LoanFilterDTO.class,
          LoanRepaymentFilterDTO.class,
          TransactionFilterDTO.class
      }
  )
  @NotNull
  @Valid
  private ReportFilterDTO reportFilters;
}
