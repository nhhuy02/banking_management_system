package com.app.bankingloanservice.entity;

import com.app.bankingloanservice.constant.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a loan repayment schedule in the banking system.
 * This entity manages periodic loan repayment schedules.
 */
@Entity
@Table(name = "loan_repayment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanRepayment extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_payment_id")
    private Long loanPaymentId;

    /**
     * The loan associated with this repayment schedule.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    /**
     * The principal amount to be repaid in this schedule.
     */
    @Column(name = "principal_amount", nullable = false)
    private BigDecimal principalAmount;

    /**
     * The interest amount to be repaid in this schedule.
     */
    @Column(name = "interest_amount", nullable = false)
    private BigDecimal interestAmount;

    /**
     * The late payment interest amount, if applicable.
     */
    @Column(name = "late_payment_interest_amount", nullable = false)
    private BigDecimal latePaymentInterestAmount;

    /**
     * The total amount to be repaid in this schedule.
     */
    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    /**
     * The due date for this repayment schedule.
     */
    @Column(name = "payment_due_date", nullable = false)
    private LocalDate paymentDueDate;

    /**
     * The actual date when the repayment was made, if applicable.
     */
    @Column(name = "actual_payment_date")
    private LocalDate actualPaymentDate;

    /**
     * The account ID used for the repayment, if applicable.
     */
    @Column(name = "account_id")
    private Long accountId;

    /**
     * The current status of the repayment schedule.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    /**
     * Indicates whether the payment is late.
     */
    @Column(name = "is_late", nullable = false)
    private Boolean isLate;

    @Transient
    public BigDecimal getTotalAmount() {
        return principalAmount.add(interestAmount).add(latePaymentInterestAmount);
    }

}