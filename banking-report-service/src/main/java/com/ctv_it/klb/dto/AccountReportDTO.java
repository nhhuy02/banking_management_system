package com.ctv_it.klb.dto;

import com.ctv_it.klb.dto.baseInfo.AccountInfoDTO;
import com.ctv_it.klb.dto.baseInfo.CustomerInfoDTO;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class AccountReportDTO {

  private CustomerInfoDTO customer;
  private List<AccountInfoDTO> accounts;
}
