package com.app.bankingloanservice.constant;

/**
 * Enum representing the status of a loan.
 */
public enum     LoanStatus {
    /**
     * The loan has been approved by the system and is pending customer approval.
     */
    PENDING,

    /**
     * The loan was cancelled before becoming active due to customer rejection or other reasons.
     */
    CANCELLED,

    /**
     * The loan is currently active and being repaid.
     */
    ACTIVE,

    /**
     * The loan was settled on time.
     */
    SETTLED_ON_TIME,

    /**
     * The loan was settled early.
     */
    SETTLED_EARLY,

    /**
     * The loan was settled late.
     */
    SETTLED_LATE,

    /**
     * The loan is past due and not yet repaid.
     */
    PAST_DUE,

    /**
     * The loan has been renewed.
     */
    RENEWAL
}
