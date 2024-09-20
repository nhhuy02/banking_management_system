package com.ctv_it.customer_service.service.kafkaconsumer;

import com.ctv_it.customer_service.dto.ChangeStatusDto;
import com.ctv_it.customer_service.mapper.CustomersStatusHistoryMapper;
import com.ctv_it.customer_service.model.Customer;
import com.ctv_it.customer_service.model.CustomersStatusHistory;
import com.ctv_it.customer_service.repository.CustomerRepository;
import com.ctv_it.customer_service.repository.CustomersStatusHistoryRepository;
import com.ctv_it.customer_service.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class CustomerStatusConsumer {

    private static final Logger logger = LoggerFactory.getLogger(CustomerStatusConsumer.class);

    @Autowired
    private CustomersStatusHistoryMapper customersStatusHistoryMapper;

    @Autowired
    private CustomersStatusHistoryRepository customersStatusHistoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @KafkaListener(topics = "account-status-topics", groupId = "customer-group")
    public void consumeCustomerStatus(String data) {
        ChangeStatusDto changeStatusDto = JsonUtil.toJson(data, ChangeStatusDto.class);
        if (changeStatusDto != null) {
            logger.info("Received change status from Kafka with accId: {} and Status: {}", changeStatusDto.getAccountId(), changeStatusDto.getStatus());
            try {
                Optional<Customer> customerOptional = customerRepository.findByAccountId(changeStatusDto.getAccountId());

                if (customerOptional.isPresent()) {
                    Customer customer = customerOptional.get();

                    CustomersStatusHistory customersStatusHistory = new CustomersStatusHistory();
                    customersStatusHistory.setCustomer(customer);

                    try {
                        CustomersStatusHistory.Status status = CustomersStatusHistory.Status.valueOf(changeStatusDto.getStatus());
                        customersStatusHistory.setStatus(status);
                    } catch (IllegalArgumentException e) {
                        logger.error("Invalid status value received: {}", changeStatusDto.getStatus());
                        return;
                    }

                    customersStatusHistory.setChangedAt(Instant.now());
                    customersStatusHistoryRepository.save(customersStatusHistory);

                    logger.info("Customer status history created successfully: {}", changeStatusDto);
                } else {
                    logger.info("Customer not found for accountId: {}", changeStatusDto.getAccountId());
                }
            } catch (Exception e) {
                logger.warn("Error saving customer status data: {}", e.getMessage());
            }
        } else {
            logger.warn("ChangeStatusDto is null, check again.");
        }
    }
}
