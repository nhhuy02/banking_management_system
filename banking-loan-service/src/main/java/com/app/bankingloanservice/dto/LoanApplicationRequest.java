package com.app.bankingloanservice.dto;

import com.app.bankingloanservice.constant.InterestRateType;
import com.app.bankingloanservice.constant.RepaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Data Transfer Object for loan application")
public class LoanApplicationRequest {

    // Customer ID (reference from Customer Service)
    @NotNull(message = "Customer ID cannot be null")
    @Schema(description = "Customer ID (reference from Customer Service)", example = "12345")
    private Long customerId;

    // Account ID for receiving disbursement and making repayments
    @NotNull(message = "Account ID cannot be null")
    @Schema(description = "Account ID for receiving disbursement and making repayments", example = "98765")
    private Long accountId;

    // Customer's monthly income
    @NotNull(message = "Monthly income cannot be null")
    @Schema(description = "Customer's monthly income", example = "20000000")
    private Long monthlyIncome;

    // Customer's occupation
    @NotNull(message = "Occupation cannot be null")
    @Schema(description = "Customer's occupation", example = "Software Engineer")
    private String occupation;

    // Loan type ID (reference to the LoanType entity)
    @NotNull(message = "Loan type ID cannot be null")
    @Schema(description = "Loan type ID (reference to the LoanType entity)", example = "1")
    private Long loanTypeId;

    // Desired loan amount
    @NotNull(message = "Desired loan amount cannot be null")
    @Schema(description = "Desired loan amount", example = "50000000")
    private Long desiredLoanAmount;

    // Desired loan term in months
    @NotNull(message = "Desired loan term months cannot be null")
    @Schema(description = "Desired loan term in months", example = "12")
    private Integer desiredLoanTermMonths;

    // Repayment method (e.g., EQUAL_INSTALLMENTS, REDUCING_BALANCE)
    @NotNull(message = "Repayment method cannot be null")
    @Schema(description = "Repayment method (e.g., EQUAL_INSTALLMENTS, REDUCING_BALANCE)", example = "EQUAL_INSTALLMENTS")
    private RepaymentMethod repaymentMethod;

    // Desired disbursement date
    @NotNull(message = "Desired disbursement date cannot be null")
    @Schema(description = "Desired disbursement date", example = "2024-10-01")
    private LocalDate desiredDisbursementDate;

    // Interest rate type (FIXED or FLOATING)
    @NotNull(message = "Interest rate type cannot be null")
    @Schema(description = "Interest rate type (FIXED or FLOATING)", example = "FIXED")
    private InterestRateType interestRateType;

    // Customer's loan purpose
    @Schema(description = "Customer's loan purpose", example = "Home Renovation")
    private String loanPurpose;

    // Collateral details associated with the loan application
    @Schema(description = "Collateral details associated with the loan application")
    private CollateralRequest collateralRequest;
}
