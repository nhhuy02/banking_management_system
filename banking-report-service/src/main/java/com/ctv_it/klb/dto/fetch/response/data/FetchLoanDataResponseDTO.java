package com.ctv_it.klb.dto.fetch.response.data;

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
public class FetchLoanDataResponseDTO {

  private long loanId;
  private long loanApplicationId;
  private long accountId;
  private String loanContractNo;
  private String loanTypeName;
  private BigDecimal loanAmount;
  private String interestRateType;
  private CurrentInterestRate currentInterestRate;
  private String repaymentMethod;
  private int loanTermMonths;
  private LocalDate disbursementDate;
  private LocalDate maturityDate;
  private LocalDate settlementDate;
  private int renewalCount;
  private BigDecimal remainingBalance;
  private BigDecimal totalPaidAmount;
  private boolean isBadDebt;
  private LocalDate badDebtDate;
  private String badDebtReason;
  private String debtClassification;
  private LoanSettlement loanSettlementResponse;
  private String status;

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Setter
  @Getter
  @ToString
  public static class CurrentInterestRate {

    private BigDecimal annualInterestRate;
    private BigDecimal pastDueInterestRate;
    private BigDecimal latePaymentInterestRate;
    private BigDecimal prepaymentPenaltyRate;
    private LocalDate effectiveFrom;
  }
//
//    @Builder
//    @Setter
//    @Getter
//    @ToString
//    public static class LoanRepayment {
//
//      private int loanPaymentId;
//      private BigDecimal principalAmount;
//      private Decimal interestAmount;
//      private Decimal latePaymentInterestAmount;
//      private BigDecimal totalAmount;
//      private LocalDate paymentDueDate;
//      private LocalDate actualPaymentDate;
//      private int accountId;
//      private LocalDate paymentStatus;
//      private boolean isLate;
//    }

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Setter
  @Getter
  @ToString
  public static class LoanSettlement {

    private int loanSettlementId;
    private LocalDate settlementDate;
    private BigDecimal pastDueInterestAmount;
    private BigDecimal latePaymentInterestAmount;
    private BigDecimal prepaymentPenaltyAmount;
    private BigDecimal settlementAmount;
    private String settlementStatus;
  }
}
