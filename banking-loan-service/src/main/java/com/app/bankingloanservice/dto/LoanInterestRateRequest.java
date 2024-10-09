package com.app.bankingloanservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object for Loan Interest Rate.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanInterestRateRequest {

    @Schema(description = "Annual interest rate as a percentage.", example = "5.00")
    @NotNull(message = "{loanInterestRateRequest.annualInterestRate.notNull}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{loanInterestRateRequest.annualInterestRate.min}")
    @Digits(integer = 3, fraction = 2, message = "{loanInterestRateRequest.annualInterestRate.digits}")
    private BigDecimal annualInterestRate;

    @Schema(description = "Past due interest rate as a percentage.", example = "6.00")
    @NotNull(message = "{loanInterestRateRequest.pastDueInterestRate.notNull}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{loanInterestRateRequest.pastDueInterestRate.min}")
    @Digits(integer = 3, fraction = 2, message = "{loanInterestRateRequest.pastDueInterestRate.digits}")
    private BigDecimal pastDueInterestRate;

    @Schema(description = "Late payment interest rate as a percentage per annum.", example = "7.00")
    @NotNull(message = "{loanInterestRateRequest.latePaymentInterestRate.notNull}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{loanInterestRateRequest.latePaymentInterestRate.min}")
    @Digits(integer = 3, fraction = 2, message = "{loanInterestRateRequest.latePaymentInterestRate.digits}")
    private BigDecimal latePaymentInterestRate;

    @Schema(description = "Prepayment penalty rate as a percentage of the remaining balance.", example = "2.00")
    @NotNull(message = "{loanInterestRateRequest.prepaymentPenaltyRate.notNull}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{loanInterestRateRequest.prepaymentPenaltyRate.min}")
    @Digits(integer = 3, fraction = 2, message = "{loanInterestRateRequest.prepaymentPenaltyRate.digits}")
    private BigDecimal prepaymentPenaltyRate;

    @Schema(description = "Effective date when the interest rate becomes applicable.", example = "2024-01-01")
    @NotNull(message = "{loanInterestRateRequest.effectiveFrom.notNull}")
    @FutureOrPresent(message = "{loanInterestRateRequest.effectiveFrom.futureOrPresent}")
    private LocalDate effectiveFrom;

    @Schema(description = "Expiration date when the interest rate becomes invalid.", example = "2024-12-31")
    @Future(message = "{loanInterestRateRequest.effectiveTo.future}")
    private LocalDate effectiveTo;
}
