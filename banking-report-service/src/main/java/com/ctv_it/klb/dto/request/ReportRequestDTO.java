package com.ctv_it.klb.dto.request;

import com.ctv_it.klb.config.validation.ValueOfEnumValidator;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import com.ctv_it.klb.dto.filter.extend.AccountFilterDTO;
import com.ctv_it.klb.dto.filter.extend.LoanFilterDTO;
import com.ctv_it.klb.dto.filter.extend.TransactionFilterDTO;
import com.ctv_it.klb.enumeration.ReportType;
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
public class ReportRequestDTO {

  @NotNull(message = "error.invalid.null")
  private Long customerId;

  @NotBlank(message = "error.invalid.blank")
  @ValueOfEnumValidator(enumClass = ReportType.class)
  @Schema(description = "Allowable values: ['ACCOUNT', 'TRANSACTION', 'LOAN']")
  private String reportType;

  @Schema(
      description = """
              Filters applied based on the report type:
              - `ACCOUNT` -> `accountFilters` : AccountFilterDTO.class
              - `LOAN` -> `loanFilters` : LoanFilterDTO.class
              - `TRANSACTION` -> `transactionFilters` : TransactionFilterDTO.class
          """,
      oneOf = {AccountFilterDTO.class, LoanFilterDTO.class, TransactionFilterDTO.class}
  )
  private ReportFilterDTO reportFilters;
}
