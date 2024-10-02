package com.app.bankingloanservice.util;

import com.app.bankingloanservice.constant.PaymentStatus;
import com.app.bankingloanservice.entity.Loan;
import com.app.bankingloanservice.entity.LoanRepayment;
import com.app.bankingloanservice.repository.LoanRepaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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

        BigDecimal principal = loan.getLoanAmount();
        int term = loan.getLoanTermMonths();
        LocalDate disbursementDate = loan.getDisbursementDate();

        BigDecimal annualInterestRate = loan.getCurrentInterestRate().getAnnualInterestRate();
        BigDecimal monthlyInterestRate = annualInterestRate.divide(BigDecimal.valueOf(12), 10, BigDecimal.ROUND_HALF_UP)
                .divide(BigDecimal.valueOf(100), 10, BigDecimal.ROUND_HALF_UP); // Chuyển từ % sang thập phân

        BigDecimal principalPerMonth = principal.divide(BigDecimal.valueOf(term), 2, BigDecimal.ROUND_HALF_UP);
        BigDecimal remainingBalance = principal;

        for (int i = 1; i <= term; i++) {
            LocalDate dueDate = disbursementDate.plusMonths(i);
            BigDecimal interest = remainingBalance.multiply(monthlyInterestRate).setScale(2, BigDecimal.ROUND_HALF_UP);
            BigDecimal totalAmount = principalPerMonth.add(interest).setScale(2, BigDecimal.ROUND_HALF_UP);

            LoanRepayment repayment = LoanRepayment.builder()
                    .loan(loan)
                    .principalAmount(principalPerMonth)
                    .interestAmount(interest)
                    .latePaymentInterestAmount(BigDecimal.ZERO)
                    .totalAmount(totalAmount)
                    .paymentDueDate(dueDate)
                    .paymentStatus(PaymentStatus.PENDING)
                    .isLate(false)
                    .build();
            repaymentSchedule.add(repayment);

            remainingBalance = remainingBalance.subtract(principalPerMonth);
        }

        return loanRepaymentRepository.saveAll(repaymentSchedule);
    }
}