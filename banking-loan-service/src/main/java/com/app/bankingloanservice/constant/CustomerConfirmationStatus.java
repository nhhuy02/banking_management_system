package com.app.bankingloanservice.constant;

/**
 * Enum representing the approval status of a loan by the customer.
 * <p>
 * This status determines whether the customer has accepted or rejected
 * the loan after it has been approved by the bank.
 */
public enum CustomerConfirmationStatus {

    /**
     * The loan is awaiting the customer's confirmation.
     */
    PENDING,

    /**
     * The customer has confirmed and accepted the loan terms.
     */
    APPROVED,

    /**
     * The customer has rejected the loan terms.
     */
    REJECTED
}

