package com.app.bankingloanservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanTypeDto {

    @Schema(description = "Unique identifier for the loan type", example = "1")
    private Long loanTypeId;

    @Schema(description = "Name of the loan type (e.g., Personal Loan, Mortgage)", example = "Personal Loan")
    private String loanTypeName;

    @Schema(description = "Annual interest rate for the loan type (%)", example = "5.00")
    private BigDecimal annualInterestRate;

    @Schema(description = "Interest rate applied to past due amounts (%)", example = "7.00")
    private BigDecimal pastDueInterestRate;

    @Schema(description = "Interest rate applied for late payments (%)", example = "10.00")
    private BigDecimal latePaymentInterestRate;

    @Schema(description = "Penalty rate for early loan repayment (%)", example = "2.00")
    private BigDecimal prepaymentPenaltyRate;

    @Schema(description = "Maximum amount that can be borrowed for this loan type (VND)", example = "500000000")
    private Long maxLoanAmount;

    @Schema(description = "Maximum term for the loan in months", example = "24")
    private Integer maxLoanTermMonths;

    @Schema(description = "Indicates whether collateral is required for this loan type", example = "true")
    private Boolean requiresCollateral;

    @Schema(description = "Time required to review the loan application (number of days)", example = "5")
    private Integer reviewTimeDays;

    @Schema(description = "Detailed description of the loan type", example = "This is a personal loan for urgent needs.")
    private String description;

    @Schema(description = "Indicates if the loan type is currently active and available", example = "true")
    private Boolean isActive;
}
