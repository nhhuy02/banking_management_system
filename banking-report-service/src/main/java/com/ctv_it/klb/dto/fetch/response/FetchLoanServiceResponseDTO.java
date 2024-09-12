package com.ctv_it.klb.dto.fetch.response;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class FetchLoanServiceResponseDTO {

  private UUID accountId;
}
