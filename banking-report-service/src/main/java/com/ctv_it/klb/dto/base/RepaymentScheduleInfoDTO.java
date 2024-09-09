package com.ctv_it.klb.dto.base;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
public class RepaymentScheduleInfoDTO {

  private UUID id;
  private LocalDateTime date;
  private BigDecimal paymentAmount;
  private BigDecimal principalAmount;
  private BigDecimal interestAmount;
  private BigDecimal remainingBalance;
  private String paymentMethod;
  private String status;
  private BigDecimal penaltyFee;
  private BigDecimal extraPayment;
  private String currency;
  private String paymentReference;
  private String description;
}
