package com.ctv_it.klb.dto.filter.extend;

import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@Setter
@Getter
@ToString(callSuper = true)
@Schema(description = "Filter for loan repayment reports")
public class LoanRepaymentFilterDTO extends ReportFilterDTO {

  @NotNull(message = "error.invalid.null")
  @Schema(description = "Id of loan")
  private Long loanId;

//  ->> upgrade later
//  @Schema(description = "Repayment status of loan", allowableValues = {"PAID", "PENDING", "OVERDUE"})
//  private Set<String> repaymentStatus;
//
//  @Schema(description = "Repayment date range")
//  private RangeDTO<LocalDate> repaymentDateRange;
}

