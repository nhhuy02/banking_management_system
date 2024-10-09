package com.ctv_it.klb.dto.filter.extend;

import com.ctv_it.klb.dto.filter.RangeDTO;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Set;
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
@Schema(description = "Filter for loan reports")
public class LoanFilterDTO extends ReportFilterDTO {

  @Schema(description = "Id of loan's type")
  private Long loanTypeId;

  @Schema(description = "Loan repayment schedule date range")
  private RangeDTO<LocalDate> loanRepaymentScheduleRange;

  @Schema(description = "Status of loan (null OR many of)", allowableValues = {"PENDING",
      "CANCELLED", "ACTIVE", "SETTLED_ON_TIME", "SETTLED_EARLY", "SETTLED_LATE", "PAST_DUE",
      "RENEWAL"})
  private Set<String> loanStatus;
}
