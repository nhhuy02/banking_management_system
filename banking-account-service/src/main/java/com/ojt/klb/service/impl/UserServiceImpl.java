package com.ojt.klb.service.impl;

import com.ojt.klb.client.AccountClient;
import com.ojt.klb.dto.*;
import com.ojt.klb.exception.PhoneNumberAlreadyExistsException;
import com.ojt.klb.exception.UserNotFoundException;
import com.ojt.klb.model.Account;
import com.ojt.klb.model.User;
import com.ojt.klb.repository.AccountRepository;
import com.ojt.klb.repository.UserRepository;
import com.ojt.klb.response.ApiResponse;
import com.ojt.klb.service.UserService;
import com.ojt.klb.utils.GenerateUniqueNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final String TOPIC = "customer-topic";
    private static final String TOPIC_SMS = "gen-code-verify-password-topic";

    private final AccountClient accountClient;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final GenerateUniqueNumber generateUniqueAccountNumber;
    private final KafkaTemplate<String, CustomerDto> kafkaTemplate;
    private final KafkaTemplate<String, AccountDto> kafkaTemplateSMS;

    public UserServiceImpl(AccountClient accountClient, UserRepository userRepository, PasswordEncoder passwordEncoder,
            AccountRepository accountRepository, GenerateUniqueNumber generateUniqueAccountNumber,
            KafkaTemplate<String, CustomerDto> kafkaTemplate, KafkaTemplate<String, AccountDto> kafkaTemplateSMS) {
        this.accountClient = accountClient;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.generateUniqueAccountNumber = generateUniqueAccountNumber;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaTemplateSMS = kafkaTemplateSMS;
    }

    @Override
    public Optional<LoginDto> login(String username, String password) {
        logger.info("Login with username: {} ", username);
        Optional<User> login = userRepository.findByUsername(username);

        if (login.isPresent()) {
            User user = login.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                logger.info("Login successful for username: {} ", username);
                LoginDto loginDto = new LoginDto();
                loginDto.setId(user.getId());
                loginDto.setUsername(user.getUsername());
                loginDto.setRole(String.valueOf(user.getRole()));
                return Optional.of(loginDto);
            } else {
                logger.warn("Login failed for username: {} ", username);
            }
        } else {
            logger.warn("Username not found: {} ", username);
        }
        return Optional.empty();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RegisterResponseDto createUser(RegisterDto registerDto) {
        if (userRepository.existsByPhoneNumber(registerDto.getPhoneNumber())) {
            logger.warn("Phone number already in use: {} ", registerDto.getPhoneNumber());
            throw new PhoneNumberAlreadyExistsException("Phone number already exists: " + registerDto.getPhoneNumber());
        }

        if (!isValidPassword(registerDto.getPassword())) {
            logger.warn("Password must be at least 8 characters long and contain a special character: {}",
                    registerDto.getPassword());
            throw new IllegalArgumentException("Password does not meet security requirements.");
        }

        User newUser = new User();
        newUser.setUsername(registerDto.getPhoneNumber());
        newUser.setPhoneNumber(registerDto.getPhoneNumber());
        newUser.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        if (newUser.getRole() == null) {
            newUser.setRole(User.Role.customer);
        }

        newUser.setCreatedAt(Timestamp.from(Instant.now()));
        userRepository.save(newUser);
        logger.info("New user created with Id: {}", newUser.getId());

        Account newAccount = new Account();
        newAccount.setUser(newUser);
        newAccount.setAccountName("TKTT KHACH HANG ");

        String accountNumber = generateUniqueAccountNumber.generate();
        newAccount.setAccountNumber(accountNumber);
        newAccount.setBalance(BigDecimal.ZERO);
        newAccount.setStatus(Account.Status.active);
        newAccount.setCreatedAt(Timestamp.from(Instant.now()));
        accountRepository.save(newAccount);

        CustomerDto customerDto = new CustomerDto();
        customerDto.setAccountId(newAccount.getId());
        customerDto.setPhoneNumber(newUser.getPhoneNumber());
        kafkaTemplate.send(TOPIC, customerDto);
        logger.info("Sent customer information to Kafka topic: {}", TOPIC);

        RegisterResponseDto response = new RegisterResponseDto();
        response.setId(newUser.getId());
        response.setUsername(newUser.getUsername());
        response.setRole(newUser.getRole().name());
        return response;
    }

    @Override
    public void forgetPasswordGetCode(String phoneNumber) {
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        if (user.isPresent()) {
            Optional<Account> account = accountRepository.findByUserId(user.get().getId());
            ResponseEntity<ApiResponse<AccountDto>> data = accountClient.getData(account.get().getId());
            if (data.getBody() != null && data.getBody().isSuccess()) {
                AccountDto accountDto = data.getBody().getData();
                accountDto.setAccountId(account.get().getId());
                if (accountDto.getPhoneNumber().equals(phoneNumber)) {
                    accountDto.setAccountId(account.get().getId());
                    kafkaTemplateSMS.send(TOPIC_SMS, accountDto);
                    logger.info("Send messages to: {}", TOPIC_SMS);
                    logger.info("Data: {}", accountDto);
                } else {
                    logger.info("Phone number does not match");
                }
            }
        } else {
            logger.warn("User not found: {} ", phoneNumber);
            throw new UserNotFoundException("User with phoneNumber " + phoneNumber + " not found.");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void changePassword(String phoneNumber, String password) {
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);

        if (user.isPresent()) {
            User existingUser = user.get();

            if (isValidPassword(password)) {
                logger.warn("Password does not meet security requirements for phone number: {}", phoneNumber);
                throw new IllegalArgumentException("Password does not meet security requirements.");
            }

            String encodedPassword = passwordEncoder.encode(password);
            existingUser.setPassword(encodedPassword);

            userRepository.save(existingUser);
            logger.info("Password for {} has been updated.", phoneNumber);

        } else {
            logger.warn("PhoneNumber not found: {}", phoneNumber);
            throw new UserNotFoundException("User with phoneNumber " + phoneNumber + " not found.");
        }
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 5 &&
                password.matches(".*[!@#$%^&*()].*") &&
                password.matches(".*[A-Z].*");
    }

}
