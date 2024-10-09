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

    @NotBlank(message = "{loan.type.name.required}")
    @Size(max = 100, message = "{loan.type.name.size}")
    @Schema(description = "Name of the loan type (e.g., Personal Loan, Commercial Loan)", example = "Personal Loan")
    private String loanTypeName;

    @NotNull(message = "{annual.interest.rate.required}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{annual.interest.rate.min}")
    @Digits(integer = 3, fraction = 2, message = "{annual.interest.rate.digits}")
    @Schema(description = "Annual interest rate for the loan type (%)", example = "5.50")
    private BigDecimal annualInterestRate;

    @NotNull(message = "{past.due.interest.rate.required}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{past.due.interest.rate.min}")
    @Digits(integer = 3, fraction = 2, message = "{past.due.interest.rate.digits}")
    @Schema(description = "Interest rate applied to past due amounts (%)", example = "2.00")
    private BigDecimal pastDueInterestRate;

    @NotNull(message = "{late.payment.interest.rate.required}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{late.payment.interest.rate.min}")
    @Digits(integer = 3, fraction = 2, message = "{late.payment.interest.rate.digits}")
    @Schema(description = "Interest rate applied to late payments (%)", example = "1.50")
    private BigDecimal latePaymentInterestRate;

    @NotNull(message = "{prepayment.penalty.rate.required}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{prepayment.penalty.rate.min}")
    @Digits(integer = 3, fraction = 2, message = "{prepayment.penalty.rate.digits}")
    @Schema(description = "Penalty rate for early repayment (%)", example = "0.50")
    private BigDecimal prepaymentPenaltyRate;

    @NotNull(message = "{max.loan.amount.required}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{max.loan.amount.min}")
    @Digits(integer = 15, fraction = 2, message = "{max.loan.amount.digits}")
    @Schema(description = "Maximum loan amount that can be borrowed for this loan type (VND)", example = "500000000.00")
    private BigDecimal maxLoanAmount;

    @NotNull(message = "{max.loan.term.required}")
    @Min(value = 1, message = "{max.loan.term.min}")
    @Schema(description = "Maximum term for the loan in months", example = "60")
    private Integer maxLoanTermMonths;

    @NotNull(message = "{collateral.requirement.required}")
    @Schema(description = "Indicates whether collateral is required for this loan type", example = "true")
    private Boolean requiresCollateral;

    @NotNull(message = "{review.time.required}")
    @Min(value = 1, message = "{review.time.min}")
    @Schema(description = "Time required for loan review (in days)", example = "3")
    private Integer reviewTimeDays;

    @Size(max = 1000, message = "{description.size}")
    @Schema(description = "Detailed description of the loan type", example = "This loan type is suitable for personal use.")
    private String description;

    @NotNull(message = "{active.status.required}")
    @Schema(description = "Indicates whether this loan type is currently active and available", example = "true")
    private Boolean isActive;
}
