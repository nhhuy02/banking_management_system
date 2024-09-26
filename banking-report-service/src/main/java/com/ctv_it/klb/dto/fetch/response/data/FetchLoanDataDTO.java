package com.ctv_it.klb.dto.fetch.response.data;

import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class FetchLoanDataDTO {

  private int loanId;
  private int customerId;
  private String loanContractNo;
  private LoanType loanType;
  private double loanAmount;
  private CurrentInterestRate currentInterestRate;
  private int loanTermMonths;
  private LocalDate disbursementDate;
  private LocalDate maturityDate;
  private LocalDate settlementDate;
  private int renewalCount;
  private double remainingBalance;
  private double totalPaidAmount;
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

    private double rate;
  }

  @Builder
  @Setter
  @Getter
  @ToString
  public static class Collateral {

    private String collateralType;
    private double collateralValue;
  }

  @Builder
  @Setter
  @Getter
  @ToString
  public static class LoanRepayment {

    private int loanPaymentId;
    private double principalAmount;
    private double interestAmount;
    private double latePaymentInterestAmount;
    private double totalAmount;
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
    private double pastDueInterestAmount;
    private double latePaymentInterestAmount;
    private double prepaymentPenaltyAmount;
    private double settlementAmount;
    private String settlementStatus;
  }
}
