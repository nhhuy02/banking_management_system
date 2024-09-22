package com.ctv_it.klb.dto.filter.extend;

import com.ctv_it.klb.config.validation.FieldName;
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
@Getter
@Setter
@ToString(callSuper = true)
@Schema(description = "Filter for account reports")
public class AccountFilterDTO extends ReportFilterDTO {

  @FieldName("accountType")
  private String accountType;
  @FieldName("accountStatus")
  private String accountStatus;
  @FieldName("openingDateRange")
  private RangeDTO<LocalDate> openingDateRange;
}
