package com.ojt.klb.banking_notification_service.service;

import com.ojt.klb.banking_notification_service.dto.consumer.loan.LoanDueDate;
import com.ojt.klb.banking_notification_service.dto.consumer.loan.LoanOverdue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LoanProducerTest {
    private static final String TOPIC = "repayment_due_notification";
    private static final String TOPIC1 = "repayment_overdue_notification";
    @Autowired
    private KafkaTemplate<String, LoanDueDate> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String, LoanOverdue> kafkaTemplate1;

    public void sendLoanData(LoanDueDate loanData) {
        kafkaTemplate.send(TOPIC, loanData);
    }

    public void sendLoanOverData(LoanOverdue loanData) {
        kafkaTemplate1.send(TOPIC1, loanData);
    }
}
