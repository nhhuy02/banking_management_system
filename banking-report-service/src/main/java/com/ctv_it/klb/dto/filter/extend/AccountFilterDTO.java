package com.ctv_it.klb.dto.filter.extend;

import com.ctv_it.klb.dto.filter.RangeDTO;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Filter for account reports")
public class AccountFilterDTO extends ReportFilterDTO {

  @NotNull
  private Long accountId;
  @NotNull
  private Long savingAccountId;
  private String accountType;
  private String accountStatus;
  private RangeDTO<LocalDate> openingDateRange;
}
