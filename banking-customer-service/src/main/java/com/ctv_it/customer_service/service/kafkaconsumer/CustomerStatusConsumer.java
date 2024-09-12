package com.ctv_it.customer_service.service.kafkaconsumer;

import com.ctv_it.customer_service.dto.CustomersStatusHistoryDto;
import com.ctv_it.customer_service.mapper.CustomersStatusHistoryMapper;
import com.ctv_it.customer_service.model.Customer;
import com.ctv_it.customer_service.model.CustomersStatusHistory;
import com.ctv_it.customer_service.repository.CustomerRepository;
import com.ctv_it.customer_service.repository.CustomersStatusHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CustomerStatusConsumer {

    private static final Logger logger = LoggerFactory.getLogger(CustomerStatusConsumer.class);

    @Autowired
    private CustomersStatusHistoryMapper customersStatusHistoryMapper;

    @Autowired
    private CustomersStatusHistoryRepository customersStatusHistoryRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @KafkaListener(topics = "customerstatus-topics", groupId = "customer-group")
    public void consumeCustomerStatus(CustomersStatusHistoryDto customersStatusHistoryDto) {
        try {

            CustomersStatusHistory customersStatusHistory = customersStatusHistoryMapper.toEntity(customersStatusHistoryDto);

            Customer customer = customerRepository.findById(customersStatusHistoryDto.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            customersStatusHistory.setCustomer(customer);

            CustomersStatusHistory.Status status = CustomersStatusHistory.Status.valueOf(customersStatusHistoryDto.getStatus());
            customersStatusHistory.setStatus(status);

            customersStatusHistory.setChangedAt(Instant.now());

            customersStatusHistoryRepository.save(customersStatusHistory);
            logger.info("Customer data saved successfully: {}", customersStatusHistoryDto);
        } catch (Exception e) {
            logger.warn("Error saving customer status data: {}", e.getMessage());
        }
    }
}
