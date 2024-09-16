package com.ctv_it.klb.dto.filter.extend;

import com.ctv_it.klb.dto.filter.RangeFilterDTO;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Setter
@Getter
@ToString(callSuper = true)
@Schema(description = "Filter for transaction reports")
public class TransactionFilterDTO extends ReportFilterDTO {

  private Long accountId;
  private String transactionType;
  private String transactionCategory;
  private RangeFilterDTO<LocalDate> transactionDate;
  private String transactionStatus;
}
