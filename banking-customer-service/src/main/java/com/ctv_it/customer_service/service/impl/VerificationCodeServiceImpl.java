package com.ctv_it.customer_service.service.impl;

import com.ctv_it.customer_service.dto.AccountData;
import com.ctv_it.customer_service.dto.OtpEmailRequestDto;
import com.ctv_it.customer_service.dto.VerificationCodeDto;
import com.ctv_it.customer_service.model.Customer;
import com.ctv_it.customer_service.model.VerificationCode;
import com.ctv_it.customer_service.repository.CustomerRepository;
import com.ctv_it.customer_service.repository.VerificationCodeRepository;
import com.ctv_it.customer_service.service.VerificationCodeService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.Random;

@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    private static final Logger logger = LoggerFactory.getLogger(VerificationCodeServiceImpl.class);
    private static final String TOPIC = "otp-email-topic";
    private static final String TOPIC1= "data-account-topic";
    private static final String CUSTOMER_SERVICE_URL = "http://localhost:8040/api/v1/account/";

    private final VerificationCodeRepository verificationCodeRepository;
    private final CustomerRepository customerRepository;
    private final KafkaTemplate<String, OtpEmailRequestDto> kafkaTemplate;
    private final KafkaTemplate<String, AccountData> kafkaTemplate1;
    private final RestTemplate restTemplate;
//    private final EmailService emailService;

    public VerificationCodeServiceImpl(VerificationCodeRepository verificationCodeRepository, CustomerRepository customerRepository, KafkaTemplate<String, OtpEmailRequestDto> kafkaTemplate, KafkaTemplate<String, AccountData> kafkaTemplate1, RestTemplate restTemplate) {
        this.verificationCodeRepository = verificationCodeRepository;
        this.customerRepository = customerRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplate1 = kafkaTemplate1;
        this.restTemplate = restTemplate;
    }

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

        // Send code via Kafka
        OtpEmailRequestDto otpEmailRequestDto = new OtpEmailRequestDto();
        otpEmailRequestDto.setCustomerId(customerId);
        otpEmailRequestDto.setCustomerName(customer.get().getFullName());
        otpEmailRequestDto.setEmail(email);
        otpEmailRequestDto.setOtpCode(code);
        logger.info("OTP is: {}", otpEmailRequestDto.getOtpCode());
        otpEmailRequestDto.setTimeToLiveCode("5 minutes");
        kafkaTemplate.send(TOPIC, otpEmailRequestDto);
        logger.info("Sent otp email to Kafka topic {}", TOPIC);

        VerificationCodeDto dto = new VerificationCodeDto();
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

        if (verificationCode.getExpiresAt().isBefore(Instant.now())) {
            logger.warn("Verification failed: Code expired for customer ID: {}", customerId);
            return false;
        }

        verificationCode.setIsVerified(true);
        verificationCode.setUsedAt(Instant.now());
        verificationCodeRepository.save(verificationCode);
        logger.info("Code verified successfully for customer ID: {}", customerId);

        Optional<Customer> customer = customerRepository.findById(customerId);
        if(customer.isPresent()) {

            String url = CUSTOMER_SERVICE_URL + customer.get().getAccountId();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                String responseBody = responseEntity.getBody();
                AccountData accountData = parseData(responseBody);

                accountData.setCustomerId(customerId);
                accountData.setCustomerName(customer.get().getFullName());
                accountData.setEmail(customer.get().getEmail());
                accountData.setAccountNumber(accountData.getAccountNumber());
                accountData.setAccountType(accountData.getAccountType());
                kafkaTemplate1.send(TOPIC1, accountData);
                logger.info("Sent otp email to Kafka topic: {}", TOPIC1);
                logger.info("Data {}", accountData);
            } else {
                logger.error("Failed to fetch customer data from external service for accountId: {}", customer.get().getAccountId());
            }
        } else {
            throw new IllegalArgumentException("Customer not found");
        }
        return true;
    }

    private AccountData parseData(String responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode dataNode = rootNode.path("data");

            AccountData accountDto = new AccountData();
            accountDto.setAccountNumber(Long.valueOf(dataNode.path("accountNumber").asText()));
            accountDto.setAccountType(dataNode.path("accountName").asText());

            return accountDto;
        } catch (Exception e) {
            logger.error("Error parsing customer data: ", e);
            return new AccountData();
        }
    }
}

