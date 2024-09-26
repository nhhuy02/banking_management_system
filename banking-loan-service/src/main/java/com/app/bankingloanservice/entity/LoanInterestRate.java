package com.app.bankingloanservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.math.BigDecimal;

/**
 * Represents the history of loan interest rate changes.
 */
@Entity
@Table(name = "loan_interest_rate")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoanInterestRate extends AuditModel {

    /**
     * Unique identifier for the loan interest rate record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_interest_rate_id")
    private Long loanInterestRateId;

    /**
     * The loan associated with this interest rate.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    /**
     * Indicates if this is the current interest rate for the loan.
     */
    @Column(name = "is_current", nullable = false)
    private Boolean isCurrent;

    /**
     * Annual interest rate as a percentage.
     */
    @Column(name = "annual_interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal annualInterestRate;

    /**
     * Past due interest rate as a percentage.
     */
    @Column(name = "past_due_interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal pastDueInterestRate;

    /**
     * Late payment interest rate as a percentage per annum.
     */
    @Column(name = "late_payment_interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal latePaymentInterestRate;

    /**
     * Prepayment penalty rate as a percentage of the remaining balance.
     */
    @Column(name = "prepayment_penalty_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal prepaymentPenaltyRate;

    /**
     * Effective date when the interest rate becomes applicable.
     */
    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    /**
     * Expiration date when the interest rate becomes invalid.
     */
    @Column(name = "effective_to")
    private LocalDate effectiveTo;

}
