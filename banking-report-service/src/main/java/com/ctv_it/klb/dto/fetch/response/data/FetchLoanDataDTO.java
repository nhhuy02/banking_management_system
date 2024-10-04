package com.ctv_it.klb.dto.fetch.response.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.poi.hpsf.Decimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@ToString
public class FetchLoanDataDTO {

  private int loanId;
  private int customerId;
  private String loanContractNo;
  private LoanType loanType;
  private BigDecimal loanAmount;
  private CurrentInterestRate currentInterestRate;
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
  private Collateral collateral;
  private List<LoanRepayment> loanRepayments;
  private LoanSettlement loanSettlement;
  private String status;

  @Builder
  @Setter
  @Getter
  @ToString
  public static class LoanType {

    private String loanTypeName;
  }

  @Builder
  @Setter
  @Getter
  @ToString
  public static class CurrentInterestRate {

    private Decimal rate;
  }

  @Builder
  @Setter
  @Getter
  @ToString
  public static class Collateral {

    private String collateralType;
    private BigDecimal collateralValue;
  }

  @Builder
  @Setter
  @Getter
  @ToString
  public static class LoanRepayment {

    private int loanPaymentId;
    private BigDecimal principalAmount;
    private Decimal interestAmount;
    private Decimal latePaymentInterestAmount;
    private BigDecimal totalAmount;
    private LocalDate paymentDueDate;
    private LocalDate actualPaymentDate;
    private int accountId;
    private LocalDate paymentStatus;
    private boolean isLate;

  }

  @Builder
  @Setter
  @Getter
  @ToString
  public static class LoanSettlement {

    private int loanSettlementId;
    private LocalDate settlementDate;
    private Decimal pastDueInterestAmount;
    private Decimal latePaymentInterestAmount;
    private BigDecimal prepaymentPenaltyAmount;
    private BigDecimal settlementAmount;
    private String settlementStatus;
  }
}
