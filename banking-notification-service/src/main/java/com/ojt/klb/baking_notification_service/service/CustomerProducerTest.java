package com.ojt.klb.baking_notification_service.service;

import com.ojt.klb.baking_notification_service.dto.CustomerData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CustomerProducer {
    private static final String TOPIC = "customer-otp-topic";


    @Autowired
    private KafkaTemplate<String, CustomerData> kafkaTemplate;

    public void sendCustomerData(CustomerData customerData) {
        kafkaTemplate.send(TOPIC, customerData);
    }
}
