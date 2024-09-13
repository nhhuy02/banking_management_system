package com.ctv_it.klb.dto.request;

import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import com.ctv_it.klb.dto.filter.extend.AccountFilterDTO;
import com.ctv_it.klb.dto.filter.extend.LoanFilterDTO;
import com.ctv_it.klb.dto.filter.extend.TransactionFilterDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class ReportRequestDTO {

  @NotBlank(message = "error.invalid.blank")
  @Pattern(regexp = "^(ACCOUNT|TRANSACTION|LOAN)$", message = "Allowable values ['ACCOUNT', 'TRANSACTION', 'LOAN']")
  private String reportType;

  @NotNull(message = "error.invalid.null")
  private Long customerId;

  @Schema(description = """
       Filters applied based on the report type:
        - `ACCOUNT` -> AccountFilterDTO
        - `LOAN` -> LoanFilterDTO
        - `TRANSACTION` -> LoanFilterDTO
      """,
      oneOf = {AccountFilterDTO.class, LoanFilterDTO.class, TransactionFilterDTO.class})
  private ReportFilterDTO reportFilters;
}
