package com.ctv_it.klb.dto.filter.extend;

import com.ctv_it.klb.dto.filter.RangeFilterDTO;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Setter
@Getter
@ToString(callSuper = true)
public class LoanFilterDTO extends ReportFilterDTO {

  private UUID customerId;
  private String type;
  private String status;
  private RangeFilterDTO<LocalDate> repaymentSchedule;
  private RangeFilterDTO<LocalDate> startDate;
  private RangeFilterDTO<LocalDate> endDate;
}
