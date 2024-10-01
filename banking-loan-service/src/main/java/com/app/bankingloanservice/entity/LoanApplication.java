package com.app.bankingloanservice.entity;

import com.app.bankingloanservice.constant.ApplicationStatus;
import com.app.bankingloanservice.constant.InterestRateType;
import com.app.bankingloanservice.constant.RepaymentMethod;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a loan application.
 */
@Entity
@Table(name = "loan_application")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplication extends AuditModel {

    /**
     * Loan application ID (primary key).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_application_id")
    private Long loanApplicationId;

    /**
     * Account ID for receiving disbursement and making repayments.
     */
    @Column(name = "account_id", nullable = false)
    private Long accountId;

    /**
     * Customer's monthly income.
     */
    @Column(name = "monthly_income", nullable = false)
    private Long monthlyIncome;

    /**
     * Customer's occupation.
     */
    @Column(name = "occupation", nullable = false)
    private String occupation;

    /**
     * Loan type (reference to the LoanType entity).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_type_id", nullable = false)
    private LoanType loanType;

    /**
     * Desired loan amount.
     */
    @Column(name = "desired_loan_amount", nullable = false)
    private Long desiredLoanAmount;

    /**
     * Desired loan term in months.
     */
    @Column(name = "desired_loan_term_months", nullable = false)
    private Integer desiredLoanTermMonths;

    /**
     * Repayment method (e.g., EQUAL_INSTALLMENTS, REDUCING_BALANCE).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "repayment_method", nullable = false)
    private RepaymentMethod repaymentMethod = RepaymentMethod.EQUAL_INSTALLMENTS;

    /**
     * Desired disbursement date.
     */
    @Column(name = "desired_disbursement_date", nullable = false)
    private LocalDate desiredDisbursementDate;

    /**
     * Interest rate type (FIXED or FLOATING).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "interest_rate_type", nullable = false)
    private InterestRateType interestRateType;

    /**
     * Customer's loan purpose.
     */
    @Column(name = "loan_purpose", columnDefinition = "TEXT")
    private String loanPurpose;

    /**
     * Customer's credit score (range: 0-750).
     * This is used to assess the customer's creditworthiness.
     */
    @Column(name = "credit_score")
    @Min(0)
    @Max(750)  // Validation to ensure the credit score is within the allowed range
    private Integer creditScore;

    /**
     * Application status (e.g., PENDING, REVIEWING, APPROVED, REJECTED, DOCUMENT_REQUIRED).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "application_status", nullable = false)
    private ApplicationStatus applicationStatus = ApplicationStatus.PENDING;

    /**
     * Loan application submission date.
     */
    @Column(name = "submission_date", nullable = false)
    private LocalDate submissionDate;

    /**
     * Review due date by the bank staff.
     */
    @Column(name = "review_due_date")
    private LocalDate reviewDueDate;

    /**
     * Loan application review date.
     */
    @Column(name = "review_date")
    private LocalDate reviewDate;

    /**
     * Review notes (reason for the decision).
     */
    @Column(name = "review_notes", columnDefinition = "TEXT")
    private String reviewNotes;

    /**
     * Associated loan for this application.
     * Cascade type is set to update the loan when the application changes,
     * but not to remove the loan when the application is deleted.
     */
    @OneToOne(mappedBy = "loanApplication", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Loan loan;

    /**
     * List of documents associated with this loan application.
     */
    @OneToMany(mappedBy = "loanApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents = new ArrayList<>();

    /**
     * List of collaterals associated with this loan application.
     */
    @OneToOne(mappedBy = "loanApplication", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collateral collateral;

}
