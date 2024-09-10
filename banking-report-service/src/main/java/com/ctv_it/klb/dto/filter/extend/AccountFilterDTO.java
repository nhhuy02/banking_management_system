package com.ctv_it.klb.dto.filter.extend;

import com.ctv_it.klb.dto.filter.RangeFilterDTO;
import com.ctv_it.klb.dto.filter.ReportFilterDTO;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@ToString(callSuper = true)
public class AccountFilterDTO extends ReportFilterDTO {

  private UUID customerId;
  private String accountType;
  private RangeFilterDTO<BigDecimal> accountBalanceRange;
  private String accountStatus;
}
