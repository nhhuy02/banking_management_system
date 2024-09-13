package com.ctv_it.klb.dto.fetch.response;

import com.ctv_it.klb.dto.base.AccountInfoDTO;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class FetchAccountResponseDTO {

  private Long customerId;
  List<AccountInfoDTO> accounts;
}
