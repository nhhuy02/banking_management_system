package com.ctv_it.klb.dto.fetch.response.data;

import com.ctv_it.klb.dto.base.AccountInfoDTO;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class FetchAccountDataDTO {

  private Long customerId;
  List<AccountInfoDTO> accounts;
}
