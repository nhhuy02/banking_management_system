package com.ctv_it.klb.dto.request;

import com.ctv_it.klb.config.validation.FieldName;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import com.ctv_it.klb.dto.filter.extend.AccountFilterDTO;
import com.ctv_it.klb.dto.filter.extend.LoanFilterDTO;
import com.ctv_it.klb.dto.filter.extend.TransactionFilterDTO;
import com.ctv_it.klb.enumeration.ReportFormat;
import com.ctv_it.klb.enumeration.ReportType;
import com.ctv_it.klb.util.ReportRequestDTODeserializerUtil;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@JsonDeserialize(using = ReportRequestDTODeserializerUtil.class)
public class ReportRequestDTO {

  @FieldName("customerId")
  private Long customerId;

  @Schema(implementation = ReportType.class)
  @FieldName("reportType")
  private ReportType reportType;

  @Schema(implementation = ReportFormat.class)
  private ReportFormat reportFormat;

  @Schema(
      description = """
              Filters applied based on the report type:
              - `ACCOUNT` -> `reportFilters` : AccountFilterDTO.class
              - `LOAN` -> `reportFilters` : LoanFilterDTO.class
              - `TRANSACTION` -> `reportFilters` : TransactionFilterDTO.class
          """,
      oneOf = {AccountFilterDTO.class, LoanFilterDTO.class, TransactionFilterDTO.class}
  )
  private ReportFilterDTO reportFilters;
}
