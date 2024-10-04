package com.app.bankingloanservice.util;

import com.app.bankingloanservice.entity.Loan;
import com.app.bankingloanservice.entity.LoanRepayment;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

public class LatePaymentInterestCalculator {

    /**
     * Calculates the late payment interest for a given repayment.
     *
     * @param loan       The loan associated with the repayment.
     * @param repayment  The repayment for which to calculate interest.
     * @return The calculated late payment interest.
     */
    public static BigDecimal calculateLatePaymentInterest(Loan loan, LoanRepayment repayment) {
        BigDecimal latePaymentInterestRate = loan.getCurrentInterestRate().getLatePaymentInterestRate();
        return repayment.getInterestAmount()
                .multiply(latePaymentInterestRate)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    /**
     * Determines if a repayment is late based on the due date and current date.
     *
     * @param repayment The repayment to check.
     * @return True if the repayment is late, false otherwise.
     */
    public static boolean isLate(LoanRepayment repayment) {
        return repayment.getPaymentDueDate().isBefore(LocalDate.now());
    }
}