package com.app.bankingloanservice.entity;

import com.app.bankingloanservice.constant.SettlementStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Represents a loan settlement in the banking loan service.
 * This entity stores information about the settlement of loans, including various fees and statuses.
 */
@Entity
@Table(name = "loan_settlement")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoanSettlement extends AuditModel {

    /**
     * The unique identifier for the loan settlement.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_settlement_id")
    private Long loanSettlementId;

    /**
     * The loan associated with this settlement.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    /**
     * The date when the loan was settled.
     */
    @Column(name = "settlement_date", nullable = false)
    private LocalDate settlementDate;

    /**
     * The amount of past-due interest at the time of settlement.
     */
    @Column(name = "past_due_interest_amount", nullable = false)
    private Long pastDueInterestAmount;

    /**
     * The amount of late payment interest at the time of settlement.
     */
    @Column(name = "late_payment_interest_amount", nullable = false)
    private Long latePaymentInterestAmount;

    /**
     * The amount of prepayment penalty at the time of settlement, if applicable.
     */
    @Column(name = "prepayment_penalty_amount", nullable = false)
    private Long prepaymentPenaltyAmount;

    /**
     * The total settlement amount, including all fees and penalties.
     */
    @Column(name = "settlement_amount", nullable = false)
    private Long settlementAmount;

    /**
     * The status of the settlement (EARLY, ON_TIME, or LATE).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "settlement_status", nullable = false)
    private SettlementStatus settlementStatus;

}