package com.app.bankingloanservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * DTO for requesting to create or update a loan type.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Payload for creating or updating a loan type.")
public class LoanTypeRequest {

    @NotBlank(message = "Loan type name is required.")
    @Size(max = 100, message = "Loan type name must not exceed 100 characters.")
    @Schema(description = "Name of the loan type (e.g., Personal Loan, Commercial Loan)", example = "Personal Loan")
    private String loanTypeName;

    @NotNull(message = "Annual interest rate is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Annual interest rate must be greater than 0.")
    @Digits(integer = 3, fraction = 2, message = "Annual interest rate must be a valid decimal number with up to two decimal places.")
    @Schema(description = "Annual interest rate for the loan type (%)", example = "5.50")
    private BigDecimal annualInterestRate;

    @NotNull(message = "Past due interest rate is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Past due interest rate must be greater than 0.")
    @Digits(integer = 3, fraction = 2, message = "Past due interest rate must be a valid decimal number with up to two decimal places.")
    @Schema(description = "Interest rate applied to past due amounts (%)", example = "2.00")
    private BigDecimal pastDueInterestRate;

    @NotNull(message = "Late payment interest rate is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Late payment interest rate must be greater than 0.")
    @Digits(integer = 3, fraction = 2, message = "Late payment interest rate must be a valid decimal number with up to two decimal places.")
    @Schema(description = "Interest rate applied to late payments (%)", example = "1.50")
    private BigDecimal latePaymentInterestRate;

    @NotNull(message = "Prepayment penalty rate is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Prepayment penalty rate must be greater than 0.")
    @Digits(integer = 3, fraction = 2, message = "Prepayment penalty rate must be a valid decimal number with up to two decimal places.")
    @Schema(description = "Penalty rate for early repayment (%)", example = "0.50")
    private BigDecimal prepaymentPenaltyRate;

    @NotNull(message = "Maximum loan amount is required.")
    @DecimalMin(value = "0.0", inclusive = false, message = "Maximum loan amount must be greater than 0.")
    @Digits(integer = 15, fraction = 2, message = "Maximum loan amount must be a valid decimal number.")
    @Schema(description = "Maximum loan amount that can be borrowed for this loan type (VND)", example = "500000000.00")
    private BigDecimal maxLoanAmount;

    @NotNull(message = "Maximum loan term (months) is required.")
    @Min(value = 1, message = "Maximum loan term must be at least 1 month.")
    @Schema(description = "Maximum term for the loan in months", example = "60")
    private Integer maxLoanTermMonths;

    @NotNull(message = "Collateral requirement is mandatory.")
    @Schema(description = "Indicates whether collateral is required for this loan type", example = "true")
    private Boolean requiresCollateral;

    @NotNull(message = "Review time is required.")
    @Min(value = 1, message = "Review time must be at least 1 day.")
    @Schema(description = "Time required for loan review (in days)", example = "3")
    private Integer reviewTimeDays;

    @Size(max = 1000, message = "Description must not exceed 1000 characters.")
    @Schema(description = "Detailed description of the loan type", example = "This loan type is suitable for personal use.")
    private String description;

    @NotNull(message = "Active status is required.")
    @Schema(description = "Indicates whether this loan type is currently active and available", example = "true")
    private Boolean isActive;
}
