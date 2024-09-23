package com.ctv_it.klb.dto.filter.extend;

import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import com.ctv_it.klb.dto.filter.RangeDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
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

  private String loanType;
  private String loanStatus;
  private RangeDTO<LocalDate> loanRepaymentScheduleRange;
  private RangeDTO<LocalDate> loanStartDateRange;
  private RangeDTO<LocalDate> loanEndDateRange;
}
