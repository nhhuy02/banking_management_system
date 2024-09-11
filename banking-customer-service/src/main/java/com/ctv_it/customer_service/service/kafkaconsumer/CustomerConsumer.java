package com.ctv_it.customer_service.service.kafkaconsumer;

import com.ctv_it.customer_service.dto.CustomerDto;
import com.ctv_it.customer_service.mapper.CustomerMapper;
import com.ctv_it.customer_service.model.Customer;
import com.ctv_it.customer_service.repository.CustomerRepository;
import com.ctv_it.customer_service.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CustomerConsumer {

    private static final Logger logger = LoggerFactory.getLogger(CustomerConsumer.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    @KafkaListener(topics = "customer-topic", groupId = "customer-group")
    public void consumeSave(CustomerDto customerDto) {
        try {
            Customer customer = customerMapper.toEntity(customerDto);
            customer.setAccountId(customerDto.getAccountId());
            customer.setPhoneNumber(customerDto.getPhoneNumber());
            customerRepository.save(customer);
            logger.info("Customer data saved successfully: {}", customerDto);
        } catch (Exception e) {
            logger.warn("Error saving customer data: {}", e.getMessage());
        }
    }
}
