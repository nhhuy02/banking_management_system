package com.ojt.klb.baking_notification_service.controller;

import com.ojt.klb.baking_notification_service.dto.consumer.CustomerData;
import com.ojt.klb.baking_notification_service.dto.consumer.LoanData;
import com.ojt.klb.baking_notification_service.dto.consumer.TransactionData;
import com.ojt.klb.baking_notification_service.service.CustomerProducerTest;
import com.ojt.klb.baking_notification_service.service.LoanProducerTest;
import com.ojt.klb.baking_notification_service.service.TransactionProducerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataProducerControllerTest {
    @Autowired
    private CustomerProducerTest producer;
    @Autowired
    private TransactionProducerTest transaction;
    @Autowired
    private LoanProducerTest loan;

    @PostMapping("/customer-producer/send-data")
    public String sendOtp(@RequestBody CustomerData customerData) {
        producer.sendCustomerData(customerData);
        return "data sent successfully";
    }
    @PostMapping("/transaction-producer/send-data")
    public String sendTrans(@RequestBody TransactionData transactionData) {
        transaction.sendTransData(transactionData);
        return "data sent successfully";
    }
    @PostMapping("/loan-producer/send-data")
    public String sendLoan(@RequestBody LoanData loanData) {
        loan.sendLoanData(loanData);
        return "data sent successfully";
    }
}
