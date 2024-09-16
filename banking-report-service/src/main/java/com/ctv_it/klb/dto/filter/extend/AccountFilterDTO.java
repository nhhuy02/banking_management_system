package com.ctv_it.klb.dto.filter.extend;

import com.ctv_it.klb.dto.filter.RangeFilterDTO;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@ToString(callSuper = true)
@Schema(description = "Filter for account reports")
public class AccountFilterDTO extends ReportFilterDTO {

  private String accountType;
  private RangeFilterDTO<BigDecimal> accountBalanceRange;
  private String accountStatus;
}
