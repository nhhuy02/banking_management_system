package com.ojt.klb.banking_notification_service.service;

import com.ojt.klb.banking_notification_service.dto.consumer.AccountData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AccountProducerTest {
    private static final String TOPIC = "account-topic";
    @Autowired
    private KafkaTemplate<String, AccountData> kafkaTemplate;
    public void sendAccountData(AccountData accountData) {
        kafkaTemplate.send(TOPIC, accountData);
    }
}
