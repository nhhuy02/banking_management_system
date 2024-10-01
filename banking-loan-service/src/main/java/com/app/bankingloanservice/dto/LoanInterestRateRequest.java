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
    @NotNull(message = "Annual interest rate is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Annual interest rate must be greater than 0")
    @Digits(integer = 3, fraction = 2, message = "Annual interest rate must be a valid percentage with up to 2 decimal places")
    private BigDecimal annualInterestRate;

    @Schema(description = "Past due interest rate as a percentage.", example = "6.00")
    @NotNull(message = "Past due interest rate is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Past due interest rate must be greater than 0")
    @Digits(integer = 3, fraction = 2, message = "Past due interest rate must be a valid percentage with up to 2 decimal places")
    private BigDecimal pastDueInterestRate;

    @Schema(description = "Late payment interest rate as a percentage per annum.", example = "7.00")
    @NotNull(message = "Late payment interest rate is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Late payment interest rate must be greater than 0")
    @Digits(integer = 3, fraction = 2, message = "Late payment interest rate must be a valid percentage with up to 2 decimal places")
    private BigDecimal latePaymentInterestRate;

    @Schema(description = "Prepayment penalty rate as a percentage of the remaining balance.", example = "2.00")
    @NotNull(message = "Prepayment penalty rate is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Prepayment penalty rate must be greater than 0")
    @Digits(integer = 3, fraction = 2, message = "Prepayment penalty rate must be a valid percentage with up to 2 decimal places")
    private BigDecimal prepaymentPenaltyRate;

    @Schema(description = "Effective date when the interest rate becomes applicable.", example = "2024-01-01")
    @NotNull(message = "Effective from date is required")
    @FutureOrPresent(message = "Effective from date cannot be in the past")
    private LocalDate effectiveFrom;

    @Schema(description = "Expiration date when the interest rate becomes invalid.", example = "2024-12-31")
    @Future(message = "Effective to date must be in the future")
    private LocalDate effectiveTo;
}
