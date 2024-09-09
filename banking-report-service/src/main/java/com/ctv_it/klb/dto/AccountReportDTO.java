package com.ctv_it.klb.dto;

import com.ctv_it.klb.dto.base.AccountInfoDTO;
import com.ctv_it.klb.dto.base.CustomerInfoDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AccountReportDTO {

  private CustomerInfoDTO customer;
  private List<AccountInfoDTO> accounts;
}
