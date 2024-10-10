package com.ctv_it.customer_service.service.kafkaconsumer;

import com.ctv_it.customer_service.dto.CustomerDto;
import com.ctv_it.customer_service.mapper.CustomerMapper;
import com.ctv_it.customer_service.model.Customer;
import com.ctv_it.customer_service.model.Kyc;
import com.ctv_it.customer_service.repository.CustomerRepository;
import com.ctv_it.customer_service.service.KycService;
import com.ctv_it.customer_service.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class CustomerConsumer {

//    private static final Logger logger = LoggerFactory.getLogger(CustomerConsumer.class);
//
//    private final CustomerRepository customerRepository;
//    private final KycService kycService;
//    private final CustomerMapper customerMapper;
//
//    public CustomerConsumer(CustomerRepository customerRepository, KycService kycService, CustomerMapper customerMapper) {
//        this.customerRepository = customerRepository;
//        this.kycService = kycService;
//        this.customerMapper = customerMapper;
//    }
//
//    @KafkaListener(topics = "customer-topic", groupId = "customer-group")
//    public void consumeSave(String data) {
//        CustomerDto customerDto = JsonUtil.toJson(data, CustomerDto.class);
//        if(customerDto != null) {
//            try {
//                Customer customer = customerMapper.toEntity(customerDto);
//                customer.setAccountId(customerDto.getAccountId());
//                customer.setPhoneNumber(customerDto.getPhoneNumber());
//                customer.setCreatedAt(Instant.now());
//
//                // Create and save KYC
//                Kyc newKyc = new Kyc();
//                newKyc.setVerificationStatus(Kyc.VerificationStatus.pending);
//                newKyc.setCreatedAt(Instant.now());
//                Kyc savedKyc = kycService.saveKyc(newKyc);
//                logger.info("Saved KYC with ID: {}", savedKyc.getId());
//                customer.setKyc(savedKyc);
//                logger.info("Set Kyc with ID: {}", customer.getKyc().getId());
//                customerRepository.save(customer);
//                logger.info("Customer data saved successfully: {}", customerDto);
//            } catch (Exception e) {
//                logger.warn("Error saving customer data: {}", e.getMessage());
//            }
//        } else {
//            logger.warn("Customer data null, check again.");
//        }
//    }
}
