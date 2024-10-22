package com.ojt.klb.banking_notification_service.controller;

import com.ojt.klb.banking_notification_service.dto.consumer.OtpEmailRequestDto;
import com.ojt.klb.banking_notification_service.dto.consumer.loan.LoanDueDate;
import com.ojt.klb.banking_notification_service.dto.consumer.loan.LoanOverdue;
import com.ojt.klb.banking_notification_service.dto.consumer.trans.TransactionInternalData;
import com.ojt.klb.banking_notification_service.service.CustomerProducerTest;
import com.ojt.klb.banking_notification_service.service.LoanProducerTest;
import com.ojt.klb.banking_notification_service.service.TransactionProducerTest;
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
    public String sendOtp(@RequestBody OtpEmailRequestDto customerData) {
        producer.sendCustomerData(customerData);
        return "data sent successfully";
    }
    @PostMapping("/loan-overdue-producer/send-data")
    public String sendTrans(@RequestBody LoanOverdue loanOverdue) {
        loan.sendLoanOverData(loanOverdue);
        return "data sent successfully";
    }
    @PostMapping("/loan-producer/send-data")
    public String sendLoan(@RequestBody LoanDueDate loanData) {
        loan.sendLoanData(loanData);
        return "data sent successfully";
    }


}
