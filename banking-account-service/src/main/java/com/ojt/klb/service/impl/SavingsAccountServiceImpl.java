package com.ojt.klb.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ojt.klb.dto.SavingsAccountResponseDto;
import com.ojt.klb.model.SavingsAccount;
import com.ojt.klb.model.User;
import com.ojt.klb.repository.AccountRepository;
import com.ojt.klb.repository.SavingsAccountRepository;
import com.ojt.klb.repository.UserRepository;
import com.ojt.klb.service.SavingsAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class SavingsAccountServiceImpl implements SavingsAccountService {

    private static final Logger logger = LoggerFactory.getLogger(SavingsAccountServiceImpl.class);
    private static final String CUSTOMER_SERVICE_URL = "http://localhost:8082/api/v1/customer/";

    private final SavingsAccountRepository savingsAccountRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final AccountRepository accountRepository;

    public SavingsAccountServiceImpl(SavingsAccountRepository savingsAccountRepository, UserRepository userRepository, RestTemplate restTemplate, AccountRepository accountRepository) {
        this.savingsAccountRepository = savingsAccountRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.accountRepository = accountRepository;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createSavingsAccount(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            if (savingsAccountRepository.existsByUserId(userId)) {
                logger.warn("Savings account already exists for userId: {}", userId);
                throw new IllegalArgumentException("Savings account already exists for this user.");
            }

            SavingsAccount savingsAccount = new SavingsAccount();
            savingsAccount.setUser(optionalUser.get());
            savingsAccount.setAccountName("Savings Account");
            savingsAccount.setBalance(BigDecimal.ZERO);
            savingsAccount.setStatus(SavingsAccount.Status.active);
            savingsAccount.setCreatedAt(Timestamp.from(Instant.now()));
            savingsAccountRepository.save(savingsAccount);
        } else {
            logger.warn("User not found for userId: {}", userId);
            throw new IllegalArgumentException("User not found.");
        }
    }

    @Override
    public Optional<SavingsAccountResponseDto> findBySavingAccountId(Long savingAccountId) {
        return savingsAccountRepository.findById(savingAccountId)
                .map(savingsAccount -> {
                    User user = savingsAccount.getUser();
                    return accountRepository.findByUserId(user.getId())
                            .map(account -> fetchCustomerData(account.getId(), savingsAccount))
                            .orElseGet(() -> {
                                logger.warn("No account found for userId: {}", user.getId());
                                return Optional.empty();
                            });
                })
                .orElseGet(() -> {
                    logger.warn("No savings account found for savingAccountId: {}", savingAccountId);
                    return Optional.empty();
                });
    }

    private Optional<SavingsAccountResponseDto> fetchCustomerData(Long accountId, SavingsAccount savingsAccount) {
        String url = CUSTOMER_SERVICE_URL + accountId;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            SavingsAccountResponseDto data = parseData(responseEntity.getBody());
            return Optional.of(createResponseDto(data, savingsAccount));
        } else {
            logger.error("Failed to fetch customer data from external service for accountId: {}", accountId);
            return Optional.empty();
        }
    }

    private SavingsAccountResponseDto createResponseDto(SavingsAccountResponseDto data, SavingsAccount savingsAccount) {
        SavingsAccountResponseDto dto = new SavingsAccountResponseDto();
        dto.setName(data.getName());
        dto.setEmail(data.getEmail());
        dto.setPhone(data.getPhone());
        dto.setAccountName(savingsAccount.getAccountName());
        dto.setBalance(savingsAccount.getBalance());
        dto.setStatus(savingsAccount.getStatus().toString());
        return dto;
    }

    private SavingsAccountResponseDto parseData(String responseBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode dataNode = rootNode.path("data");

            SavingsAccountResponseDto savingsAccountResponseDto = new SavingsAccountResponseDto();
            savingsAccountResponseDto.setName(dataNode.path("fullName").asText());
            savingsAccountResponseDto.setEmail(dataNode.path("email").asText());
            savingsAccountResponseDto.setPhone(dataNode.path("phoneNumber").asText());

            return savingsAccountResponseDto;
        } catch (Exception e) {
            logger.error("Error parsing customer data: ", e);
            return new SavingsAccountResponseDto();
        }
    }
}
