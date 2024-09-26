package com.ctv_it.klb.dto.fetch.response.data;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class FetchAccountDataDTO {

  private Long id;
  private String accountName;
  private long accountNumber;
  private long balance;
  private String status;
  private LocalDateTime openingDate;
}
