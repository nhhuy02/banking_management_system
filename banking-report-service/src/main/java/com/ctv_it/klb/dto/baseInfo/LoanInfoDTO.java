package com.ctv_it.klb.dto.baseInfo;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class LoanInfoDTO {

  private Long id;
  private String loanContractNo;
  private String loanType;
  private BigDecimal amount;
  private BigDecimal interestRate;
  private int loanTerm;
  private LocalDate disbursementDate;
  private LocalDate maturityDate;
  private LocalDate settlementDate;
  private LocalDate repaymentSchedule;
  private String repaymentMethod;
  private int loanTermPaid;
  private BigDecimal amountPaid;
  private BigDecimal remainingBalance;
  private BigDecimal latePaymentPenalty;
  private boolean isBadDebt;
  private LocalDate badDebtDate;
  private String badDebtReason;
  private String debtClassification;
  private String status;

  @Builder
  @Setter
  @Getter
  @ToString
  public static class LoanRepaymentInfoDTO {

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
}
