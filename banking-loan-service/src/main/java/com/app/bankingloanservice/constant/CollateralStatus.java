package com.app.bankingloanservice.constant;

/**
 * Enum representing the possible statuses of a collateral.
 */
public enum CollateralStatus {
    /**
     * The collateral is currently active and associated with a loan.
     */
    ACTIVE,

    /**
     * The collateral has been reclaimed by the bank due to loan default.
     */
    RECLAIMED,

    /**
     * The collateral has been released back to the borrower after loan settlement.
     */
    RELEASED
}
