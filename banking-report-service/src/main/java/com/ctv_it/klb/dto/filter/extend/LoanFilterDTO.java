package com.ctv_it.klb.dto.filter.extend;

import com.ctv_it.klb.dto.filter.RangeFilterDTO;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Setter
@Getter
@ToString(callSuper = true)
public class LoanFilterDTO extends ReportFilterDTO {

  private String loanType;
  private String loanStatus;
  private RangeFilterDTO<LocalDate> loanRepaymentSchedule;
  private RangeFilterDTO<LocalDate> loanStartDate;
  private RangeFilterDTO<LocalDate> loanEndDate;
}
