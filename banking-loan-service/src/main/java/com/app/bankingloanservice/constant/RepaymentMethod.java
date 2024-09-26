package com.app.bankingloanservice.constant;

/**
 * Enum representing the method of repayment for a loan.
 */
public enum RepaymentMethod {
    /**
     * Repayment method where equal installments are paid each period.
     */
    EQUAL_INSTALLMENTS,
    /**
     * Repayment method where the balance is reduced with each payment.
     */
    REDUCING_BALANCE
}
