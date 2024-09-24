package com.ojt.klb.banking_notification_service.service;


import com.ojt.klb.banking_notification_service.dto.consumer.TransactionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TransactionProducerTest {
    private static final String TOPIC = "transaction-topic";

    @Autowired
    private KafkaTemplate<String, TransactionData> kafkaTemplate;

    public void sendTransData(TransactionData transactionData) {
        kafkaTemplate.send(TOPIC, transactionData);
    }
}
