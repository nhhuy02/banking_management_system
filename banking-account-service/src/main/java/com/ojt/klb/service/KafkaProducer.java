package com.ojt.klb.service;

import com.ojt.klb.dto.CustomerDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    @Autowired
    private KafkaTemplate<String, CustomerDto> kafkaTemplate;

    private static final String TOPIC = "customer-topic";

    public void sendCustomerData(CustomerDto customerDto) {
        logger.info("Sending customer data to Kafka: {}", customerDto);
        kafkaTemplate.send(TOPIC, customerDto);
    }
}

