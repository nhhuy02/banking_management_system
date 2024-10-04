package com.app.bankingloanservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class LoanInterestRateResponse {

    /**
     * Unique identifier for the loan interest rate record.
     */
    @Schema(description = "Unique identifier for the loan interest rate record.", example = "1")
    private Long loanInterestRateId;

    /**
     * Unique identifier for the loan associated with this interest rate.
     */
    @Schema(description = "Unique identifier for the loan associated with this interest rate.", example = "1001")
    private Long loanId;

    /**
     * Annual interest rate as a percentage.
     */
    @Schema(description = "Annual interest rate as a percentage.", example = "5.00")
    private BigDecimal annualInterestRate;

    /**
     * Past due interest rate as a percentage.
     */
    @Schema(description = "Past due interest rate as a percentage.", example = "6.00")
    private BigDecimal pastDueInterestRate;

    /**
     * Late payment interest rate as a percentage per annum.
     */
    @Schema(description = "Late payment interest rate as a percentage per annum.", example = "7.00")
    private BigDecimal latePaymentInterestRate;

    /**
     * Prepayment penalty rate as a percentage of the remaining balance.
     */
    @Schema(description = "Prepayment penalty rate as a percentage of the remaining balance.", example = "2.00")
    private BigDecimal prepaymentPenaltyRate;

    /**
     * Effective date when the interest rate becomes applicable.
     */
    @Schema(description = "Effective date when the interest rate becomes applicable.", example = "2024-01-01")
    private LocalDate effectiveFrom;

    /**
     * Expiration date when the interest rate becomes invalid.
     */
    @Schema(description = "Expiration date when the interest rate becomes invalid.", example = "2024-12-31")
    private LocalDate effectiveTo;

}
