package com.ojt.klb.banking_notification_service.service;

import com.ojt.klb.banking_notification_service.dto.consumer.OtpEmailRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class CustomerProducerTest {
    private static final String TOPIC = "customer-otp-topic";


    @Autowired
    private KafkaTemplate<String, OtpEmailRequestDto> kafkaTemplate;

    public void sendCustomerData(OtpEmailRequestDto customerData) {
        kafkaTemplate.send(TOPIC, customerData);
    }
}
