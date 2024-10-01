package com.app.bankingloanservice.dto;

import com.app.bankingloanservice.constant.InterestRateType;
import com.app.bankingloanservice.constant.RepaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

/**
 * DTO for creating a loan.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequest { // tính năng

    @Schema(description = "ID of the loan application associated with this loan", example = "2")
    @NotNull(message = "Loan application ID is required")
    private Long loanApplicationId;

    @Schema(description = "ID of the account who is taking the loan", example = "456")
    @NotNull(message = "Account ID is required")
    private Long accountId;

    @Schema(description = "Loan type ID", example = "3")
    @NotNull(message = "Loan type ID is required")
    private Long loanTypeId;

    @Schema(description = "Amount of the loan in VND", example = "100000000")
    @NotNull(message = "Loan amount is required")
    @Min(value = 1000000, message = "Loan amount must be at least 1,000,000 VND")
    private Long loanAmount;

    @Schema(description = "Type of interest rate, either FIXED or FLOATING", example = "FIXED")
    @NotNull(message = "Interest rate type is required")
    private InterestRateType interestRateType;

    @Schema(description = "Interest rate details for the loan")
    @NotNull(message = "Loan interest rate details are required")
    private LoanInterestRateRequest currentInterestRate;

    @Schema(description = "Repayment method for the loan", example = "EQUAL_INSTALLMENTS")
    @NotNull(message = "Repayment method is required")
    private RepaymentMethod repaymentMethod;

    @Schema(description = "Term of the loan in months", example = "60")
    @NotNull(message = "Loan term is required")
    @Min(value = 6, message = "Loan term must be at least 6 months")
    @Max(value = 360, message = "Loan term cannot exceed 360 months")
    private Integer loanTermMonths;

    @Schema(description = "Disbursement date of the loan", example = "2023-10-01")
    @NotNull(message = "Disbursement date is required")
    @FutureOrPresent(message = "Disbursement date cannot be in the past")
    private LocalDate disbursementDate;
}
