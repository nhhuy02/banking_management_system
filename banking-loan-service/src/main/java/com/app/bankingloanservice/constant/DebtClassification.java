package com.app.bankingloanservice.constant;

/**
 * Enum representing the classification of debt.
 */
public enum DebtClassification {
    /**
     * Normal debt with no issues.
     */
    NORMAL,
    /**
     * Special mention debt that needs monitoring.
     */
    SPECIAL_MENTION,
    /**
     * Substandard debt that requires more attention.
     */
    SUBSTANDARD,
    /**
     * Doubtful debt that may not be recoverable.
     */
    DOUBTFUL,
    /**
     * Loss debt that is unlikely to be recovered.
     */
    LOSS
}
