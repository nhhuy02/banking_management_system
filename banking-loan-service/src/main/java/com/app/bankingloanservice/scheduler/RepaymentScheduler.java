package com.app.bankingloanservice.scheduler;

import com.app.bankingloanservice.entity.LoanRepayment;
import com.app.bankingloanservice.constant.PaymentStatus;
import com.app.bankingloanservice.repository.LoanRepaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RepaymentScheduler {

    private final LoanRepaymentRepository loanRepaymentRepository;

    /**
     * Scheduler runs every day at 2 a.m. to check for late payments.
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void checkLateRepayments() {
        LocalDate today = LocalDate.now();
        List<LoanRepayment> lateRepayments = loanRepaymentRepository.findAll()
                .stream()
                .filter(rep -> rep.getPaymentDueDate().isBefore(today) && rep.getPaymentStatus() == PaymentStatus.PENDING)
                .toList();

        for (LoanRepayment repayment : lateRepayments) {
            repayment.setIsLate(true);

            // Calculate interest and penalties if necessary
            // repayment.setLatePaymentInterestAmount(...);

            loanRepaymentRepository.save(repayment);
            log.info("Repayment ID {} is now marked as late.", repayment.getLoanPaymentId());
        }
    }
}