package com.ojt.klb.banking_notification_service.service;

import com.ojt.klb.banking_notification_service.dto.consumer.LoanData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class LoanProducerTest {
    private static final String TOPIC = "loan-topic";

    @Autowired
    private KafkaTemplate<String, LoanData> kafkaTemplate;

    public void sendLoanData(LoanData loanData) {
        kafkaTemplate.send(TOPIC, loanData);
    }
}
