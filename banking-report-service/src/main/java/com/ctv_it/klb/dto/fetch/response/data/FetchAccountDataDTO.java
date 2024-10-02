package com.ctv_it.klb.dto.fetch.response.data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class FetchAccountDataDTO {

  private Long id;
  private String accountName;
  private String accountNumber;
  private BigDecimal balance;
  private String status;
  private LocalDateTime openingDate;
}
