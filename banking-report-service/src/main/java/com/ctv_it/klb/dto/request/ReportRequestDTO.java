package com.ctv_it.klb.dto.request;

import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import com.ctv_it.klb.dto.filter.extend.AccountFilterDTO;
import com.ctv_it.klb.dto.filter.extend.LoanFilterDTO;
import com.ctv_it.klb.dto.filter.extend.TransactionFilterDTO;
import com.ctv_it.klb.util.ReportRequestDTODeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@JsonDeserialize(using = ReportRequestDTODeserializer.class)
public class ReportRequestDTO {

  @NotNull(message = "error.invalid.null")
  private Long customerId;

  @NotBlank(message = "error.invalid.blank")
  @Schema(description = "Allowable values: ['ACCOUNT', 'TRANSACTION', 'LOAN']")
  private String reportType;

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
