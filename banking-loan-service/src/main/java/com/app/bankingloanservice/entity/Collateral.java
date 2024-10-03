package com.app.bankingloanservice.entity;

import com.app.bankingloanservice.constant.CollateralStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Represents a collateral entity in the banking loan service.
 * This entity stores information about collaterals associated with loans or loan applications.
 */
@Entity
@Table(name = "collateral")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Collateral extends AuditModel {

    /**
     * The unique identifier for the collateral.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "collateral_id")
    private Long collateralId;

    /**
     * The loan associated with this collateral.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    /**
     * The loan application associated with this collateral.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_application_id")
    private LoanApplication loanApplication;

    /**
     * Document associated with this collateral.
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "document_id")
    private Document document;

    /**
     * The type of collateral (e.g., house, car, valuable papers).
     */
    @Column(name = "collateral_type", nullable = false)
    private String collateralType;

    /**
     * The value of the collateral in VND.
     */
    @Column(name = "collateral_value", nullable = false)
    private BigDecimal collateralValue;

    /**
     * Detailed description of the collateral.
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * The current status of the collateral.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CollateralStatus status = CollateralStatus.ACTIVE;

    /**
     * The date when the collateral was reclaimed, if applicable.
     */
    @Column(name = "reclaim_date")
    private LocalDate reclaimDate;

    /**
     * The reason for reclamation of the collateral, if applicable.
     */
    @Column(name = "reason_for_reclamation")
    private String reasonForReclamation;

    /**
     * The date when the collateral was released, if applicable.
     */
    @Column(name = "release_date")
    private LocalDate releaseDate;

}