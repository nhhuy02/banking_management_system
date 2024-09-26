package com.app.bankingloanservice.entity;

import com.app.bankingloanservice.constant.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a loan entity in the banking system.
 */
@Entity
@Table(name = "loan")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Loan extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id")
    private Long loanId;

    /**
     * The loan application associated with this loan.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_application_id", nullable = false)
    private LoanApplication loanApplication;

    /**
     * The ID of the customer who took the loan.
     */
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    /**
     * Unique contract number for the loan.
     */
    @Column(name = "loan_contract_no", nullable = false, unique = true, length = 20)
    private String loanContractNo;

    /**
     * The customer's confirmation status.
     * Possible values: PENDING, APPROVED, REJECTED.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "customer_confirmation_status", nullable = false)
    private CustomerConfirmationStatus customerConfirmationStatus;

    /**
     * The date when the customer confirmation.
     */
    @Column(name = "customer_confirmation_date")
    private LocalDate customerConfirmationDate;

    /**
     * The type of loan.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_type_id", nullable = false)
    private LoanType loanType;

    /**
     * The amount of the loan in VND.
     */
    @Column(name = "loan_amount", nullable = false)
    private Long loanAmount;

    /**
     * The type of interest rate (fixed or floating).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "interest_rate_type", nullable = false)
    private InterestRateType interestRateType;

    /**
     * The current interest rate details for the loan.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_interest_rate_id", nullable = false)
    private LoanInterestRate currentInterestRate;

    /**
     * The history of interest rates applied to this loan.
     */
    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LoanInterestRate> interestRateHistory;

    /**
     * The method of repayment for the loan.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "repayment_method", nullable = false)
    private RepaymentMethod repaymentMethod;

    /**
     * The term of the loan in months.
     */
    @Column(name = "loan_term_months", nullable = false)
    private Integer loanTermMonths;

    /**
     * The date when the loan was disbursed.
     */
    @Column(name = "disbursement_date", nullable = false)
    private LocalDate disbursementDate;

    /**
     * The date when the loan is due to be fully repaid.
     */
    @Column(name = "maturity_date", nullable = false)
    private LocalDate maturityDate;

    /**
     * The date when the loan was actually settled, if applicable.
     */
    @Column(name = "settlement_date")
    private LocalDate settlementDate;

    /**
     * The number of times the loan has been renewed.
     */
    @Column(name = "renewal_count", nullable = false)
    private Integer renewalCount;

    /**
     * The remaining balance of the loan in VND.
     */
    @Column(name = "remaining_balance", nullable = false)
    private Long remainingBalance;

    /**
     * The total amount that has been paid towards the loan in VND.
     */
    @Column(name = "total_paid_amount", nullable = false)
    private Long totalPaidAmount;

    /**
     * Indicates whether the loan is classified as a bad debt.
     */
    @Column(name = "is_bad_debt", nullable = false)
    private Boolean isBadDebt;

    /**
     * The date when the loan was classified as a bad debt, if applicable.
     */
    @Column(name = "bad_debt_date")
    private LocalDate badDebtDate;

    /**
     * The reason for classifying the loan as a bad debt, if applicable.
     */
    @Column(name = "bad_debt_reason")
    private String badDebtReason;

    /**
     * The current classification of the debt.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "debt_classification", nullable = false)
    private DebtClassification debtClassification;

    /**
     * List of collaterals associated with this loan application.
     */
    @OneToOne(mappedBy = "loan", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Collateral collateral;

    /**
     * List of loan repayments associated with this loan application.
     */
    @OneToMany(mappedBy = "loan", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<LoanRepayment> loanRepayments = new ArrayList<>();

    /**
     * List of loan repayments associated with this loan application.
     */
    @OneToOne(mappedBy = "loan", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private LoanSettlement loanSettlement;

    /**
     * The current status of the loan.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LoanStatus status;

}