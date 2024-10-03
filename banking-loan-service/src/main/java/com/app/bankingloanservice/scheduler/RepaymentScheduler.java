package com.app.bankingloanservice.scheduler;

import com.app.bankingloanservice.entity.LoanRepayment;
import com.app.bankingloanservice.constant.PaymentStatus;
import com.app.bankingloanservice.repository.LoanRepaymentRepository;
import com.app.bankingloanservice.entity.Loan;
import com.app.bankingloanservice.util.LatePaymentInterestCalculator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RepaymentScheduler {

    private final LoanRepaymentRepository loanRepaymentRepository;

    /**
     * Scheduler runs every day at 2 A.M to check for late payments.
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void checkLateRepayments() {
        LocalDate today = LocalDate.now();
        List<LoanRepayment> lateRepayments = loanRepaymentRepository.findByPaymentDueDateBeforeAndPaymentStatus(today, PaymentStatus.PENDING);

        for (LoanRepayment repayment : lateRepayments) {
            // Calculate late payment interest using the calculator
            Loan loan = repayment.getLoan();
            BigDecimal latePaymentInterest = LatePaymentInterestCalculator.calculateLatePaymentInterest(loan, repayment);
            repayment.setLatePaymentInterestAmount(latePaymentInterest);
            repayment.setPaymentStatus(PaymentStatus.OVERDUE);

            // Optionally update any other fields or notify users

            loanRepaymentRepository.save(repayment);
            log.info("Repayment ID {} is now marked as overdue with late interest {}", repayment.getLoanPaymentId(), latePaymentInterest);
        }
    }
}