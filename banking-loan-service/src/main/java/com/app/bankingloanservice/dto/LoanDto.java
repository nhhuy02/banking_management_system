package com.app.bankingloanservice.dto;

import com.app.bankingloanservice.constant.*;
import com.app.bankingloanservice.entity.LoanInterestRate;
import com.app.bankingloanservice.entity.LoanRepayment;
import com.app.bankingloanservice.entity.LoanSettlement;
import com.app.bankingloanservice.entity.LoanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanDto {

    private Long loanId;

    // Loan application details
    private Long loanApplicationId;

    // Customer ID
    private Long customerId;

    // Loan contract number
    private String loanContractNo;

    // Customer confirmation status
    private CustomerConfirmationStatus customerConfirmationStatus;

    // Date of customer confirmation
    private LocalDate customerConfirmationDate;

    // Loan type
    private LoanType loanType;

    // Loan amount in VND
    private Long loanAmount;

    // Interest rate type
    private InterestRateType interestRateType;

    // Current interest rate details
    private LoanInterestRate currentInterestRate;

    // Repayment method
    private RepaymentMethod repaymentMethod;

    // Loan term in months
    private Integer loanTermMonths;

    // Date of disbursement
    private LocalDate disbursementDate;

    // Loan maturity date
    private LocalDate maturityDate;

    // Date of settlement
    private LocalDate settlementDate;

    // Loan renewal count
    private Integer renewalCount;

    // Remaining loan balance in VND
    private Long remainingBalance;

    // Total amount paid in VND
    private Long totalPaidAmount;

    // Whether the loan is bad debt
    private Boolean isBadDebt;

    // Date when loan became bad debt
    private LocalDate badDebtDate;

    // Reason for bad debt classification
    private String badDebtReason;

    // Debt classification
    private DebtClassification debtClassification;

    // Collateral details
    private CollateralDto collateral;

    // List of repayments
    private List<LoanRepayment> loanRepayments;

    // Loan settlement details
    private LoanSettlement loanSettlement;

    // Loan status
    private LoanStatus status;
}

