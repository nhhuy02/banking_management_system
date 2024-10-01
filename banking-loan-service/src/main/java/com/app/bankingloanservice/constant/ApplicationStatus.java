package com.app.bankingloanservice.constant;

/**
 * Enum representing the status of a loan application.
 */
public enum ApplicationStatus {
    /**
     * The application is pending review.
     */
    PENDING,
    /**
     * Additional documents are required for the application.
     */
    DOCUMENT_REQUIRED,
    /**
     * The application is being reviewed.
     */
    REVIEWING,
    /**
     * The application has been approved.
     */
    APPROVED,
    /**
     * The application has been rejected.
     */
    REJECTED,
    /**
     * The application has passed the approval deadline without being approved.
     */
    EXPIRED
}
