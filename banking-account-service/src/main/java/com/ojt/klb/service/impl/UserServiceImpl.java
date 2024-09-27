package com.ojt.klb.service.impl;

import com.ojt.klb.dto.CustomerDto;
import com.ojt.klb.dto.LoginDto;
import com.ojt.klb.dto.RegisterDto;
import com.ojt.klb.dto.RegisterResponseDto;
import com.ojt.klb.exception.PhoneNumberAlreadyExistsException;
import com.ojt.klb.exception.UserNotFoundException;
import com.ojt.klb.model.Account;
import com.ojt.klb.model.User;
import com.ojt.klb.repository.AccountRepository;
import com.ojt.klb.repository.UserRepository;
import com.ojt.klb.service.UserService;
import com.ojt.klb.utils.GenerateUniqueNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final GenerateUniqueNumber generateUniqueAccountNumber;
    private final KafkaTemplate<String, CustomerDto> kafkaTemplate;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AccountRepository accountRepository, GenerateUniqueNumber generateUniqueAccountNumber, KafkaTemplate<String, CustomerDto> kafkaTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.generateUniqueAccountNumber = generateUniqueAccountNumber;
        this.kafkaTemplate = kafkaTemplate;
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
        newAccount.setAccountNumber(Long.parseLong(accountNumber));
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
    @Transactional(rollbackFor = Exception.class)
    public void forgetPassword(Long userId, String newPassword) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            user.get().setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user.get());
            logger.info("Updated password for user: {}", user.get().getUsername());
        } else {
            throw new UserNotFoundException("User with ID " + userId + " not found.");
        }
    }
}
