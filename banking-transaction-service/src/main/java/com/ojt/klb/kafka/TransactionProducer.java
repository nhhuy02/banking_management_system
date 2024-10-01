package com.ojt.klb.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionProducer {
    private final KafkaTemplate<String, TransactionNotification> kafkaTemplate;

    public void sendTransactionNotification(TransactionNotification transactionNotification) {
        log.info("Sending transaction notification");
        Message<TransactionNotification> message = MessageBuilder
                .withPayload(transactionNotification)
                .setHeader(TOPIC, "transaction-topic")
                .build();

        kafkaTemplate.send(message);
    }
}
