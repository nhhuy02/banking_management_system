package com.ctv_it.klb.dto.base;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class LoanInfoDTO {

  private Long id;
  private String loanType;
  private BigDecimal amount;
  private BigDecimal interestRate;
  private int loanTerm;
  private LocalDate startDate;
  private LocalDate endDate;
  private LocalDate maturityDate;
  private LocalDate repaymentSchedule;
  private int loanTermPaid;
  private BigDecimal amountPaid;
  private String status;
  private BigDecimal latePaymentPenalty;
}
