package com.ctv_it.customer_service.service.impl;

import com.ctv_it.customer_service.client.AccountClient;
import com.ctv_it.customer_service.dto.AccountData;
import com.ctv_it.customer_service.dto.OtpRequestDto;
import com.ctv_it.customer_service.dto.VerificationCodeRequestDto;
import com.ctv_it.customer_service.model.Customer;
import com.ctv_it.customer_service.model.VerificationCode;
import com.ctv_it.customer_service.repository.CustomerRepository;
import com.ctv_it.customer_service.repository.VerificationCodeRepository;
import com.ctv_it.customer_service.response.ApiResponse;
import com.ctv_it.customer_service.service.VerificationCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private static final Logger logger = LoggerFactory.getLogger(VerificationCodeServiceImpl.class);
    private static final String TOPIC = "otp-email-topic";
    private static final String TOPIC1= "data-account-topic";

    private final AccountClient accountClient;
    private final VerificationCodeRepository verificationCodeRepository;
    private final CustomerRepository customerRepository;
    private final KafkaTemplate<String, OtpRequestDto> kafkaTemplate;
    private final KafkaTemplate<String, AccountData> kafkaTemplate1;
//    private final EmailService emailService;

    public VerificationCodeServiceImpl(AccountClient accountClient, VerificationCodeRepository verificationCodeRepository, CustomerRepository customerRepository, KafkaTemplate<String, OtpRequestDto> kafkaTemplate, KafkaTemplate<String, AccountData> kafkaTemplate1) {
        this.accountClient = accountClient;
        this.verificationCodeRepository = verificationCodeRepository;
        this.customerRepository = customerRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplate1 = kafkaTemplate1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public VerificationCodeRequestDto generateCode(Long customerId, String email) {
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

        // Send code via Kafka
        OtpRequestDto otpEmailRequestDto = new OtpRequestDto();
        otpEmailRequestDto.setCustomerId(customerId);
        otpEmailRequestDto.setCustomerName(customer.get().getFullName());
        otpEmailRequestDto.setEmail(email);
        otpEmailRequestDto.setOtpCode(code);
        logger.info("OTP is: {}", otpEmailRequestDto.getOtpCode());
        otpEmailRequestDto.setTimeToLiveCode("5 minutes");
        kafkaTemplate.send(TOPIC, otpEmailRequestDto);
        logger.info("Sent otp email to Kafka topic {}", TOPIC);

        VerificationCodeRequestDto dto = new VerificationCodeRequestDto();
        dto.setEmail(email);
        return dto;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean verifyCode(Long customerId, String code) {
        Optional<VerificationCode> verificationCodeOptional = verifyCodeAndCheckExpiry(customerId, code);
        if (verificationCodeOptional.isEmpty()) {
            return false;
        }

        Optional<Customer> customerOptional = customerRepository.findById(customerId);
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            ResponseEntity<ApiResponse<AccountData>> responseEntity = accountClient.getAccountData(customer.getAccountId());

            if (responseEntity.getBody() != null && responseEntity.getBody().isSuccess()) {
                AccountData accountData = responseEntity.getBody().getData();
                if (accountData != null) {
                    accountData.setCustomerId(customerId);
                    accountData.setCustomerName(customer.getFullName());
                    accountData.setEmail(customer.getEmail());
                    accountData.setPhoneNumber(customer.getPhoneNumber());

                    kafkaTemplate1.send(TOPIC1, accountData);
                    logger.info("Sent OTP email to Kafka topic: {}", TOPIC1);
                    return true;
                }
            }
            logger.error("Failed to fetch customer data from external service for accountId: {}", customer.getAccountId());
        } else {
            throw new IllegalArgumentException("Customer not found");
        }

        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean verifyOtpResetPassword(Long customerId, String code) {
        return verifyCodeAndCheckExpiry(customerId, code).isPresent();
    }

    private Optional<VerificationCode> verifyCodeAndCheckExpiry(Long customerId, String code) {
        logger.info("Verifying code for customer ID: {}", customerId);

        Optional<VerificationCode> verificationCodeOptional = verificationCodeRepository.findByCustomerIdAndCode(customerId, code);

        if (verificationCodeOptional.isEmpty()) {
            logger.warn("Verification failed: Code not found or invalid for customer ID: {}", customerId);
            return Optional.empty();
        }

        VerificationCode verificationCode = verificationCodeOptional.get();

        if (verificationCode.getExpiresAt().isBefore(Instant.now())) {
            logger.warn("Verification failed: Code expired for customer ID: {}", customerId);
            return Optional.empty();
        }

        verificationCode.setIsVerified(true);
        verificationCode.setUsedAt(Instant.now());
        verificationCodeRepository.save(verificationCode);

        logger.info("Code verified successfully for customer ID: {}", customerId);
        return Optional.of(verificationCode);
    }

}

