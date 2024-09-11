package com.ctv_it.klb.dto;

import com.ctv_it.klb.dto.base.AccountInfoDTO;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class AccountReportDTO {

  private UUID customerId;
  private int total;
  private Map<String, AccountInfoDTO> accounts; // keys = account types
}
