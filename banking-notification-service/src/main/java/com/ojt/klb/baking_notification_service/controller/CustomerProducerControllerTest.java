package com.ojt.klb.baking_notification_service.controller;

import com.ojt.klb.baking_notification_service.dto.consumer.CustomerData;
import com.ojt.klb.baking_notification_service.service.CustomerProducerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerProducerControllerTest {
    @Autowired
    private CustomerProducerTest producer;

    @PostMapping("/send-data")
    public String sendOtp(@RequestBody CustomerData customerData) {
        producer.sendCustomerData(customerData);
        return "data sent successfully";
    }
}
