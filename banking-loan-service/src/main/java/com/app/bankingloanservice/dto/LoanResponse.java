package com.app.bankingloanservice.dto;

import com.app.bankingloanservice.constant.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanResponse {

    private Long loanId;

    // Loan application details
    private Long loanApplicationId;

    // Account ID
    private Long accountId;

    private String customerName;

    private String accountNumber; // Account number for receiving disbursement and making repayments

    private String contactPhone; // Customer's phone number

    private String contactEmail; // Customer's email

    // Loan contract number
    private String loanContractNo;

    // Customer confirmation status
    private CustomerConfirmationStatus customerConfirmationStatus;

    // Date of customer confirmation
    private LocalDate customerConfirmationDate;

    // Name of Loan Type
    private String loanTypeName;

    // Loan amount in VND
    private Long loanAmount;

    // Interest rate type
    private InterestRateType interestRateType;

    // Current interest rate details
    private LoanInterestRateResponse currentInterestRate;

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

    // Loan settlement details
    private LoanSettlementResponse loanSettlementResponse;

    // Loan status
    private LoanStatus status;
}

