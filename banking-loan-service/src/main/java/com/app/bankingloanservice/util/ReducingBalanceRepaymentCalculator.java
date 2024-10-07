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

@Component("REDUCING_BALANCE")
@RequiredArgsConstructor
public class ReducingBalanceRepaymentCalculator implements RepaymentCalculator {

    private final LoanRepaymentRepository loanRepaymentRepository;

    @Override
    public List<LoanRepayment> calculateRepaymentSchedule(Loan loan) {
        List<LoanRepayment> repaymentSchedule = new ArrayList<>();

        // Get loan details
        BigDecimal principal = loan.getLoanAmount();
        int term = loan.getLoanTermMonths();
        LocalDate disbursementDate = loan.getDisbursementDate();

        // Convert annual interest rate to monthly interest rate
        BigDecimal annualInterestRate = loan.getCurrentInterestRate().getAnnualInterestRate();
        BigDecimal monthlyInterestRate = annualInterestRate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP); // Convert from percentage to decimal

        // Calculate principal to pay each month
        BigDecimal principalPerMonth = principal.divide(BigDecimal.valueOf(term), 0, RoundingMode.HALF_UP); // No decimal places
        BigDecimal remainingBalance = principal;

        // Loop through each month to create repayment schedule
        for (int i = 1; i <= term; i++) {
            LocalDate dueDate = disbursementDate.plusMonths(i);

            // Calculate interest for the current remaining balance and round to whole number
            BigDecimal interest = remainingBalance.multiply(monthlyInterestRate).setScale(0, RoundingMode.HALF_UP);

            // Calculate total amount to be paid this month (principal + interest)
            BigDecimal totalAmount = principalPerMonth.add(interest).setScale(0, RoundingMode.HALF_UP);

            // Create LoanRepayment entry for this month
            LoanRepayment repayment = LoanRepayment.builder()
                    .loan(loan)
                    .accountId(loan.getAccountId())
                    .principalAmount(principalPerMonth) // Monthly principal repayment
                    .interestAmount(interest) // Monthly interest amount
                    .latePaymentInterestAmount(BigDecimal.ZERO) // Default late payment interest as zero
                    .totalAmount(totalAmount) // Total payment for this month
                    .paymentDueDate(dueDate) // Due date for this payment
                    .paymentStatus(PaymentStatus.PENDING) // Set status to pending
                    .isLate(false) // Initially, no late payment
                    .build();
            repaymentSchedule.add(repayment); // Add repayment to schedule

            // Update remaining balance after the principal payment
            remainingBalance = remainingBalance.subtract(principalPerMonth).setScale(0, RoundingMode.HALF_UP);
        }

        // Save the repayment schedule
        return loanRepaymentRepository.saveAll(repaymentSchedule);
    }
}
