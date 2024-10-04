package com.ctv_it.klb.dto.filter.extend;

import com.ctv_it.klb.dto.filter.RangeDTO;
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
@Schema(description = "Filter for transaction reports")
public class TransactionFilterDTO extends ReportFilterDTO {

  private String transactionType;
  private String transactionCategory;
  private RangeDTO<LocalDate> transactionDateRange;
  private String transactionStatus;
}
