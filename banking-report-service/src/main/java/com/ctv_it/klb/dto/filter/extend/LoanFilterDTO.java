package com.ctv_it.klb.dto.filter.extend;

import com.ctv_it.klb.dto.filter.RangeFilterDTO;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
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
  private RangeFilterDTO<LocalDate> loanRepaymentSchedule;
  private RangeFilterDTO<LocalDate> loanStartDate;
  private RangeFilterDTO<LocalDate> loanEndDate;
}
