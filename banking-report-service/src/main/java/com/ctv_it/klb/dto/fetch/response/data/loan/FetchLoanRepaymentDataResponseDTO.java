package com.ctv_it.klb.dto.fetch.response.data.loan;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class FetchLoanRepaymentDataResponseDTO {

  private long loanId;
  private long loanPaymentId;
  private BigDecimal principalAmount;
  private BigDecimal interestAmount;
  private BigDecimal latePaymentInterestAmount;
  private BigDecimal totalAmount;
  private LocalDate paymentDueDate;
  private LocalDate actualPaymentDate;
  private String transactionReference;
  private boolean isLate;
  private String paymentStatus;
}

