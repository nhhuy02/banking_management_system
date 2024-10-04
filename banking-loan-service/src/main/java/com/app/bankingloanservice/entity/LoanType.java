package com.app.bankingloanservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "loan_type")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoanType extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_type_id")
    private Long loanTypeId;

    // Name of the loan type (e.g., Personal Loan, Mortgage)
    @Column(name = "loan_type_name", nullable = false, unique = true)
    private String loanTypeName;

    // Annual interest rate for the loan type (%)
    @Column(name = "annual_interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal annualInterestRate;

    // Interest rate applied to past due amounts (%)
    @Column(name = "past_due_interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal pastDueInterestRate;

    // Interest rate applied for late payments (%)
    @Column(name = "late_payment_interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal latePaymentInterestRate;

    // Penalty rate for early loan repayment (%)
    @Column(name = "prepayment_penalty_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal prepaymentPenaltyRate;

    // Maximum amount that can be borrowed for this loan type (VND)
    @Column(name = "max_loan_amount", nullable = false)
    private BigDecimal maxLoanAmount;

    // Maximum term for the loan in months
    @Column(name = "max_loan_term_months", nullable = false)
    private Integer maxLoanTermMonths;

    // Indicates whether collateral is required for this loan type
    @Column(name = "requires_collateral", nullable = false)
    private Boolean requiresCollateral;

    // Time required to review the loan application (number of days)
    @Column(name = "review_time_days", nullable = false)
    private Integer reviewTimeDays;

    // Detailed description of the loan type
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // Indicates if the loan type is currently active and available
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    // Do not respectively remove loanApplication
    @OneToMany(mappedBy = "loanType", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<LoanApplication> loanApplications;

    //Do not respectively remove loan
    @OneToMany(mappedBy = "loanType", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Loan> loans;

}