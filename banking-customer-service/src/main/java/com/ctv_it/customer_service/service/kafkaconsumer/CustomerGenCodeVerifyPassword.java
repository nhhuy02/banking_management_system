package com.ctv_it.customer_service.service.kafkaconsumer;

import com.ctv_it.customer_service.dto.CustomerDto;
import com.ctv_it.customer_service.dto.OtpRequestDto;
import com.ctv_it.customer_service.model.Customer;
import com.ctv_it.customer_service.model.VerificationCode;
import com.ctv_it.customer_service.repository.CustomerRepository;
import com.ctv_it.customer_service.repository.VerificationCodeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

@Service
public class CustomerGenCodeVerifyPassword {

    private static final Logger logger = LoggerFactory.getLogger(CustomerGenCodeVerifyPassword.class);
    private static final String TOPIC = "otp-change-password";

    private final ObjectMapper objectMapper;
    private final CustomerRepository customerRepository;
    private final VerificationCodeRepository verificationCodeRepository;
    private final KafkaTemplate<String, OtpRequestDto> kafkaTemplate;

    public CustomerGenCodeVerifyPassword(CustomerRepository customerRepository, VerificationCodeRepository verificationCodeRepository, KafkaTemplate<String, OtpRequestDto> kafkaTemplate, ObjectMapper objectMapper) {
        this.customerRepository = customerRepository;
        this.verificationCodeRepository = verificationCodeRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }


    @KafkaListener(topics = "gen-code-verify-password-topic", groupId = "customer-group")
    public void genCodeVerifyChangePassword(String data) {
        try {
            logger.info("Data raw: {}", data);

            CustomerDto customerDto = objectMapper.readValue(data, CustomerDto.class);
            logger.info("Parsed CustomerDto: {}", customerDto);

            if (customerDto != null && customerDto.getAccountId() != null) {
                Optional<Customer> customerOptional = customerRepository.findById(customerDto.getAccountId());

                if (customerOptional.isPresent()) {
                    Customer customer = customerOptional.get();

                    if (customer.getPhoneNumber().equals(customerDto.getPhoneNumber())) {

                        String code = String.format("%06d", new Random().nextInt(999999));
                        logger.debug("Generated OTP code: {}", code);

                        Instant now = Instant.now();
                        Instant expiresAt = now.plus(5, ChronoUnit.MINUTES);

                        VerificationCode verificationCode = new VerificationCode();
                        verificationCode.setCustomer(customer);
                        verificationCode.setCode(code);
                        verificationCode.setCreatedAt(now);
                        verificationCode.setExpiresAt(expiresAt);
                        verificationCode.setIsVerified(false);

                        verificationCodeRepository.save(verificationCode);
                        logger.info("Verification code saved for customer ID: {}", customer.getId());

                        OtpRequestDto otpChangePassword = new OtpRequestDto();
                        otpChangePassword.setCustomerId(customerDto.getAccountId());
                        otpChangePassword.setCustomerName(customer.getFullName());
                        otpChangePassword.setEmail(customer.getEmail());
                        otpChangePassword.setOtpCode(code);
                        otpChangePassword.setTimeToLiveCode("5 minutes");
                        logger.info("OTP generated: {}", otpChangePassword.getOtpCode());

                        kafkaTemplate.send(TOPIC, otpChangePassword);
                        logger.info("Sent OTP to Kafka topic {} with data: {}", TOPIC, otpChangePassword);

                    } else {
                        logger.warn("Phone number mismatch. Customer phone: {}, Received phone: {}",
                                customer.getPhoneNumber(), customerDto.getPhoneNumber());
                    }
                } else {
                    logger.warn("Customer not found with account ID: {}", customerDto.getAccountId());
                }
            } else {
                logger.warn("Received null or invalid CustomerDto: {}", customerDto);
            }

        } catch (Exception e) {
            logger.error("Error processing customer data: {}", e.getMessage(), e);
        }
    }


}
