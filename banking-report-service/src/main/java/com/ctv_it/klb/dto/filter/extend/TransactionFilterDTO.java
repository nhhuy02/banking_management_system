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

  @Schema(description = "Type of transaction (null OR one of)", allowableValues = {"DEPOSIT",
      "WITHDRAWAL", "INTERNAL_TRANSFER", "EXTERNAL_TRANSFER", "UTILITY_PAYMENT"})
  private String transactionType;

  @Schema(description = "Transaction date range")
  private RangeDTO<LocalDate> transactionDateRange;

  @Schema(description = "Status of transaction (null OR one of)", allowableValues = {"COMPLETED",
      "PENDING", "FAILED"})
  private String transactionStatus;
}
