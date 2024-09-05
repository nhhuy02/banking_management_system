package com.ctv_it.klb.dto.fetch.response;

import com.ctv_it.klb.dto.base.AccountInfoDTO;
import java.util.List;
import java.util.UUID;
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
public class FetchAccountServiceResponseDTO {

  private UUID customerId;
  List<AccountInfoDTO> accountInfoDTOS;
}
