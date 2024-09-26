package com.app.bankingloanservice.dto;

import com.app.bankingloanservice.constant.InterestRateType;
import com.app.bankingloanservice.constant.RepaymentMethod;
import lombok.*;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * Data Transfer Object for loan application.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoanApplicationRequestDto {

    /**
     * Customer ID (reference from Customer Service).
     */
    @NotNull(message = "Customer ID cannot be null")
    private Long customerId;

    /**
     * Account ID for receiving disbursement and making repayments.
     */
    @NotNull(message = "Account ID cannot be null")
    private Long accountId;

    /**
     * Customer's monthly income.
     */
    @NotNull(message = "Monthly income cannot be null")
    private Long monthlyIncome;

    /**
     * Customer's occupation.
     */
    @NotNull(message = "Occupation cannot be null")
    private String occupation;

    /**
     * Loan type ID (reference to the LoanType entity).
     */
    @NotNull(message = "Loan type ID cannot be null")
    private Long loanTypeId;

    /**
     * Desired loan amount.
     */
    @NotNull(message = "Desired loan amount cannot be null")
    private Long desiredLoanAmount;

    /**
     * Desired loan term in months.
     */
    @NotNull(message = "Desired loan term months cannot be null")
    private Integer desiredLoanTermMonths;

    /**
     * Repayment method (e.g., EQUAL_INSTALLMENTS, REDUCING_BALANCE).
     */
    @NotNull(message = "Repayment method cannot be null")
    private RepaymentMethod repaymentMethod;

    /**
     * Desired disbursement date.
     */
    @NotNull(message = "Desired disbursement date cannot be null")
    private LocalDate desiredDisbursementDate;

    /**
     * Interest rate type (FIXED or FLOATING).
     */
    @NotNull(message = "Interest rate type cannot be null")
    private InterestRateType interestRateType;

    /**
     * Customer's loan purpose.
     */
    private String loanPurpose;

    /**
     * Collateral details associated with the loan application.
     */
    private CollateralDto collateralDto;

}
