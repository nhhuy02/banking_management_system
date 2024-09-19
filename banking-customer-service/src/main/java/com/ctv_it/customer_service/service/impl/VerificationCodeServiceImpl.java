package com.ctv_it.customer_service.service.impl;

import com.ctv_it.customer_service.dto.OtpEmailRequestDto;
import com.ctv_it.customer_service.dto.VerificationCodeDto;
import com.ctv_it.customer_service.model.Customer;
import com.ctv_it.customer_service.model.VerificationCode;
import com.ctv_it.customer_service.repository.CustomerRepository;
import com.ctv_it.customer_service.repository.VerificationCodeRepository;
import com.ctv_it.customer_service.service.VerificationCodeService;
import com.ctv_it.customer_service.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private static final Logger logger = LoggerFactory.getLogger(VerificationCodeServiceImpl.class);

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private KafkaTemplate<String, OtpEmailRequestDto> kafkaTemplate;

    private static final String TOPIC = "otp-email-topic";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VerificationCodeDto generateCode(Long customerId, String email) {
        logger.info("Generating verification code for customer ID: {} and email: {}", customerId, email);

        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            logger.error("Customer not found for ID: {}", customerId);
            throw new IllegalArgumentException("Customer not found");
        }

        if (!customer.get().getEmail().equals(email)) {
            logger.error("Email mismatch for customer ID: {}", customerId);
            throw new IllegalArgumentException("Email does not match customer");
        }

        String code = String.format("%06d", new Random().nextInt(999999));
        logger.debug("Generated code: {}", code);

        Instant now = Instant.now();
        Instant expiresAt = now.plus(5, ChronoUnit.MINUTES);

        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setCustomer(customer.get());
        verificationCode.setCode(code);
        verificationCode.setCreatedAt(now);
        verificationCode.setExpiresAt(expiresAt);
        verificationCode.setIsVerified(false);

        verificationCodeRepository.save(verificationCode);
        logger.info("Verification code saved for customer ID: {}", customerId);

        //send code
        //emailService.sendCode(email, "Banking", "Your verification code is: " + code);

        OtpEmailRequestDto otpEmailRequestDto = new OtpEmailRequestDto();
        otpEmailRequestDto.setCustomerId(customerId);
        otpEmailRequestDto.setCustomerName(customer.get().getFullName());
        otpEmailRequestDto.setEmail(email);
        otpEmailRequestDto.setOtpCode(code);
        otpEmailRequestDto.setTimeToLiveCode("5 minutes");
        kafkaTemplate.send(TOPIC, otpEmailRequestDto);
        logger.info("Sent otp email to Kafka topic {}", TOPIC);

        VerificationCodeDto dto = new VerificationCodeDto();
        dto.setCustomerId(customerId);
        dto.setCode(code);
        dto.setEmail(email);
        return dto;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean verifyCode(Long customerId, String code) {
        logger.info("Verifying code for customer ID: {}", customerId);

        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findByCustomerIdAndCode(customerId, code);

        if (verificationCodeOptional.isEmpty()) {
            logger.warn("Verification failed: Code not found or invalid for customer ID: {}", customerId);
            return false;
        }

        VerificationCode verificationCode = verificationCodeOptional.get();

        logger.info("Code expiration time: {}", verificationCode.getExpiresAt());
        logger.info("Current time: {}", Instant.now());

        if (verificationCode.getExpiresAt().isBefore(Instant.now())) {
            logger.warn("Verification failed: Code expired for customer ID: {}", customerId);
            return false;
        }

        verificationCode.setIsVerified(true);
        verificationCode.setUsedAt(Instant.now());
        verificationCodeRepository.save(verificationCode);
        logger.info("Code verified successfully for customer ID: {}", customerId);

        return true;
    }
}
