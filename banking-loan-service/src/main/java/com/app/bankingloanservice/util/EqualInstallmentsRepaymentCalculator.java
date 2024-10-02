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

@Component("EQUAL_INSTALLMENTS")
@RequiredArgsConstructor
public class EqualInstallmentsRepaymentCalculator implements RepaymentCalculator {

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

        BigDecimal emi = calculateEMI(principal, monthlyInterestRate, term);

        for (int i = 1; i <= term; i++) {
            LocalDate dueDate = disbursementDate.plusMonths(i);
            LoanRepayment repayment = LoanRepayment.builder()
                    .loan(loan)
                    .principalAmount(principal.divide(BigDecimal.valueOf(term), 2, BigDecimal.ROUND_HALF_UP))
                    .interestAmount(principal.multiply(monthlyInterestRate).setScale(2, BigDecimal.ROUND_HALF_UP))
                    .latePaymentInterestAmount(BigDecimal.ZERO)
                    .totalAmount(emi)
                    .paymentDueDate(dueDate)
                    .paymentStatus(PaymentStatus.PENDING)
                    .isLate(false)
                    .build();
            repaymentSchedule.add(repayment);
        }

        return loanRepaymentRepository.saveAll(repaymentSchedule);
    }

    private BigDecimal calculateEMI(BigDecimal principal, BigDecimal monthlyInterestRate, int term) {
        if (monthlyInterestRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(BigDecimal.valueOf(term), 2, BigDecimal.ROUND_HALF_UP);
        }
        BigDecimal numerator = monthlyInterestRate.multiply(principal)
                .multiply((BigDecimal.ONE.add(monthlyInterestRate)).pow(term));
        BigDecimal denominator = (BigDecimal.ONE.add(monthlyInterestRate)).pow(term).subtract(BigDecimal.ONE);
        return numerator.divide(denominator, 2, BigDecimal.ROUND_HALF_UP);
    }
}