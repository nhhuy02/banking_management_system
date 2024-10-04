package com.app.bankingloanservice.scheduler;

import com.app.bankingloanservice.client.account.AccountClientService;
import com.app.bankingloanservice.client.account.dto.AccountDto;
import com.app.bankingloanservice.constant.PaymentStatus;
import com.app.bankingloanservice.dto.kafka.DueDateNotificationProducer;
import com.app.bankingloanservice.dto.kafka.OverdueNotificationProducer;
import com.app.bankingloanservice.entity.LoanRepayment;
import com.app.bankingloanservice.exception.KafkaNotificationException;
import com.app.bankingloanservice.repository.LoanRepaymentRepository;
import com.app.bankingloanservice.util.LatePaymentInterestCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final KafkaTemplate<String, DueDateNotificationProducer> dueDateKafkaTemplate;
    private final KafkaTemplate<String, OverdueNotificationProducer> overdueKafkaTemplate;
    private final AccountClientService accountClientService;
    private static final String REPAYMENT_DUE_TOPIC = "repayment_due_notification";
    private static final String REPAYMENT_OVERDUE_TOPIC = "repayment_overdue_notification";

    /**
     * Scheduler runs every day at 2 A.M to check for late payments.
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void checkLateRepayments() {
        LocalDate today = LocalDate.now();
        List<LoanRepayment> lateRepayments = loanRepaymentRepository.findByPaymentDueDateBeforeAndPaymentStatus(today, PaymentStatus.PENDING);

        for (LoanRepayment repayment : lateRepayments) {
            try {
                // Calculate late payment interest
                BigDecimal latePaymentInterest = LatePaymentInterestCalculator.calculateLatePaymentInterest(repayment.getLoan(), repayment);
                repayment.setLatePaymentInterestAmount(latePaymentInterest);
                repayment.setPaymentStatus(PaymentStatus.OVERDUE);

                // Save the changes
                loanRepaymentRepository.save(repayment);
                log.info("Repayment ID {} is now marked as overdue with late interest {}", repayment.getLoanPaymentId(), latePaymentInterest);

                // Send overdue notification
                sendOverdueNotification(repayment);
            } catch (Exception e) {
                log.error("Error processing late repayment for Repayment ID {}: {}", repayment.getLoanPaymentId(), e.getMessage(), e);
            }
        }
    }

    /**
     * Scheduler runs every day at 1 A.M to check for upcoming due payments.
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void checkUpcomingDueDates() {
        LocalDate today = LocalDate.now();
        LocalDate upcomingDate = today.plusDays(3); // Example: notify 3 days before due date

        List<LoanRepayment> upcomingRepayments = loanRepaymentRepository.findByPaymentDueDate(upcomingDate, PaymentStatus.PENDING);

        for (LoanRepayment repayment : upcomingRepayments) {
            try {
                // Send due date notification
                sendDueDateNotification(repayment);
                log.info("Due date notification sent for Repayment ID {}", repayment.getLoanPaymentId());
            } catch (Exception e) {
                log.error("Error processing upcoming due date for Repayment ID {}: {}", repayment.getLoanPaymentId(), e.getMessage(), e);
            }
        }
    }

    private void sendDueDateNotification(LoanRepayment repayment) {
        try {
            // Call account service to retrieve account information
            AccountDto accountInfo = accountClientService.getAccountInfoById(repayment.getLoan().getAccountId());

            // Use builder to create notification
            DueDateNotificationProducer notification = DueDateNotificationProducer.builder()
                    .loanRepaymentId(repayment.getLoanPaymentId())
                    .accountId(repayment.getLoan().getAccountId())
                    .customerName(accountInfo.getFullName())
                    .email(accountInfo.getEmail())
                    .dueDate(repayment.getPaymentDueDate())
                    .amountDue(repayment.getPrincipalAmount().add(repayment.getInterestAmount()))
                    .build();

            dueDateKafkaTemplate.send(REPAYMENT_DUE_TOPIC, notification);
        } catch (Exception e) {
            log.error("Error sending due date notification for Repayment ID {}: {}", repayment.getLoanPaymentId(), e.getMessage(), e);
            throw new KafkaNotificationException("Failed to send due date notification", e);
        }
    }

    private void sendOverdueNotification(LoanRepayment repayment) {
        try {
            // Call account service to retrieve account information
            AccountDto accountInfo = accountClientService.getAccountInfoById(repayment.getLoan().getAccountId());

            // Use builder to create notification
            OverdueNotificationProducer notification = OverdueNotificationProducer.builder()
                    .loanRepaymentId(repayment.getLoanPaymentId())
                    .accountId(repayment.getLoan().getAccountId())
                    .customerName(accountInfo.getFullName())
                    .email(accountInfo.getEmail())
                    .dueDate(repayment.getPaymentDueDate())
                    .overdueDate(LocalDate.now())
                    .lateInterestAmount(repayment.getLatePaymentInterestAmount())
                    .totalAmountDue(repayment.getTotalAmount())
                    .build();

            overdueKafkaTemplate.send(REPAYMENT_OVERDUE_TOPIC, notification);
        } catch (Exception e) {
            log.error("Error sending overdue notification for Repayment ID {}: {}", repayment.getLoanPaymentId(), e.getMessage(), e);
            throw new KafkaNotificationException("Failed to send overdue notification", e);
        }
    }

}
