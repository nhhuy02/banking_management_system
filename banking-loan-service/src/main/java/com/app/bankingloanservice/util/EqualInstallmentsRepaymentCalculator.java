package com.app.bankingloanservice.util;

import com.app.bankingloanservice.constant.PaymentStatus;
import com.app.bankingloanservice.entity.Loan;
import com.app.bankingloanservice.entity.LoanRepayment;
import com.app.bankingloanservice.repository.LoanRepaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component("equalInstallments")
@RequiredArgsConstructor
public class EqualInstallmentsRepaymentCalculator implements RepaymentCalculator {

    private final LoanRepaymentRepository loanRepaymentRepository;

    @Override
    public List<LoanRepayment> calculateRepaymentSchedule(Loan loan) {
        List<LoanRepayment> repaymentSchedule = new ArrayList<>();

        // Get loan principal amount and term (in months)
        BigDecimal principal = loan.getLoanAmount();
        int term = loan.getLoanTermMonths();
        LocalDate disbursementDate = loan.getDisbursementDate();

        // Get the current annual interest rate and convert it to a monthly rate
        BigDecimal annualInterestRate = loan.getCurrentInterestRate().getAnnualInterestRate();
        BigDecimal monthlyInterestRate = annualInterestRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP); // Convert percentage to decimal

        // Calculate the EMI without rounding
        BigDecimal emi = calculateEMI(principal, monthlyInterestRate, term);

        // Remaining balance after each installment
        BigDecimal remainingBalance = principal;

        // Loop through each month to calculate the repayment schedule
        for (int i = 1; i <= term; i++) {
            LocalDate dueDate = disbursementDate.plusMonths(i);

            // Calculate interest amount for the current month without rounding
            BigDecimal interestAmount = remainingBalance.multiply(monthlyInterestRate);

            // Calculate principal repayment for the current month
            BigDecimal principalAmount = emi.subtract(interestAmount);

            // For the last installment, adjust the principal and interest to eliminate the remaining balance
            if (i == term) {
                principalAmount = remainingBalance;
                emi = principalAmount.add(interestAmount);
                remainingBalance = BigDecimal.ZERO;
            } else {
                // Update the remaining balance after principal repayment
                remainingBalance = remainingBalance.subtract(principalAmount);
            }

            // Round the amounts to whole VND
            principalAmount = principalAmount.setScale(0, RoundingMode.HALF_UP);
            interestAmount = interestAmount.setScale(0, RoundingMode.HALF_UP);
            emi = emi.setScale(0, RoundingMode.HALF_UP);

            // Create a new LoanRepayment entry for the current month
            LoanRepayment repayment = LoanRepayment.builder()
                    .loan(loan)
                    .accountId(loan.getAccountId())
                    .principalAmount(principalAmount) // Monthly principal repayment
                    .interestAmount(interestAmount) // Monthly interest amount
                    .latePaymentInterestAmount(BigDecimal.ZERO) // Default late payment interest as zero
                    .totalAmount(emi) // Total EMI for the month
                    .paymentDueDate(dueDate) // Due date for this repayment
                    .paymentStatus(PaymentStatus.PENDING) // Set status to pending
                    .isLate(false) // Initially, no late payment
                    .build();
            repaymentSchedule.add(repayment); // Add the repayment to the schedule
        }

        // Save the repayment schedule to the repository
        return loanRepaymentRepository.saveAll(repaymentSchedule);
    }

    // Method to calculate the EMI using the principal, monthly interest rate, and loan term
    private BigDecimal calculateEMI(BigDecimal principal, BigDecimal monthlyInterestRate, int term) {
        // If the interest rate is 0, simply divide the principal by the term
        if (monthlyInterestRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(BigDecimal.valueOf(term), 10, RoundingMode.HALF_UP);
        }
        // EMI calculation formula: EMI = (P * r * (1+r)^n) / ((1+r)^n - 1)
        BigDecimal numerator = monthlyInterestRate.multiply(principal)
                .multiply((BigDecimal.ONE.add(monthlyInterestRate)).pow(term));
        BigDecimal denominator = (BigDecimal.ONE.add(monthlyInterestRate)).pow(term).subtract(BigDecimal.ONE);
        return numerator.divide(denominator, 10, RoundingMode.HALF_UP); // Keep high precision before rounding
    }
}