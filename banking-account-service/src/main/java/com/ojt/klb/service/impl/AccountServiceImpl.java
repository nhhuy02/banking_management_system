package com.ojt.klb.service.impl;

import com.ojt.klb.client.AccountClient;
import com.ojt.klb.dto.*;
import com.ojt.klb.exception.AccountNotFoundException;
import com.ojt.klb.exception.ResourceNotFound;
import com.ojt.klb.mapper.AccountMapper;
import com.ojt.klb.model.Account;
import com.ojt.klb.model.SavingsAccount;
import com.ojt.klb.repository.AccountRepository;
import com.ojt.klb.repository.SavingsAccountRepository;
import com.ojt.klb.response.ApiResponse;
import com.ojt.klb.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {

    private final static Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
    private static final String TOPIC = "account-status-topics";

    private final AccountClient accountClient;
    private final AccountRepository accountRepository;
    private final SavingsAccountRepository savingsAccountRepository;
    private final KafkaTemplate<String, ChangeStatusDto> kafkaTemplate;
    private final AccountMapper accountMapper;


    public AccountServiceImpl(AccountClient accountClient, AccountRepository accountRepository, SavingsAccountRepository savingsAccountRepository,
                              KafkaTemplate<String, ChangeStatusDto> kafkaTemplate, AccountMapper accountMapper) {
        this.accountClient = accountClient;
        this.accountRepository = accountRepository;
        this.savingsAccountRepository = savingsAccountRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.accountMapper = accountMapper;
    }

    @Override
    public Optional<AccountDto> getAccountById(Long id) {
        return getAccountDto(id);
    }

    private Optional<AccountDto> getAccountDto(Long id) {
        Optional<Account> accountOptional = accountRepository.findById(id);

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            if (account.getStatus() == Account.Status.suspended) {
                logger.warn("Account is suspended for id: {}", id);
                return Optional.empty();
            }

            ResponseEntity<ApiResponse<AccountDto>> responseEntity = accountClient.getData(account.getId());
            if (responseEntity.getBody() != null && responseEntity.getBody().isSuccess()) {
                AccountDto customerData = responseEntity.getBody().getData();

                if (customerData != null) {
                    getData(customerData, account.getAccountName(), account.getAccountNumber());
                    customerData.setBalance(account.getBalance());
                    customerData.setStatus(account.getStatus());
                    customerData.setOpeningDate(account.getCreatedAt());

                    return Optional.of(customerData);
                } else {
                    logger.error("Failed to fetch customer data from external service for account id: {}", account.getId());
                    return Optional.empty();
                }
            }
        }
        logger.warn("Account not found for id: {}", id);
        return Optional.empty();
    }

    @Override
    public void changeStatusAccount(Long id, ChangeStatusDto changeStatusDto) {
        Account account = accountRepository.findById(id).get();
        account.setStatus(Account.Status.valueOf(changeStatusDto.getStatus()));
        accountRepository.save(account);

        changeStatusDto.setAccountId(account.getId());
        changeStatusDto.setStatus(String.valueOf(account.getStatus()));
        kafkaTemplate.send(TOPIC, changeStatusDto);
        logger.info("Send data {} and {}", changeStatusDto.getAccountId(), changeStatusDto.getStatus());
        logger.info("Sent customer information to Kafka topic: {}", TOPIC);
    }

    @Override
    public Optional<Long> getAccountIdByAccountNumber(String accountNumber) {
        Optional<Account> accountOptional = accountRepository.findAccountByAccountNumber(accountNumber);
        if (accountOptional.isPresent()) {
            return Optional.of(accountOptional.get().getId());
        } else {
            logger.warn("Account not found for account number: {}", accountNumber);
            return Optional.empty();
        }
    }

    @Override
    public Optional<AccountDto> getDataByAccountNumber(Long accountId) {
        return getAccountDto(accountId);
    }


    private void getData(AccountDto customerData, String accountName, String accountNumber) {
        customerData.setFullName(customerData.getFullName());
        customerData.setAccountName(accountName);
        customerData.setAccountNumber(accountNumber);
        customerData.setDateOfBirth(customerData.getDateOfBirth());
        customerData.setGender(customerData.getGender());
        customerData.setEmail(customerData.getEmail());
        customerData.setPhoneNumber(customerData.getPhoneNumber());
        customerData.setPermanentAddress(customerData.getPermanentAddress());
        customerData.setCurrentAddress(customerData.getCurrentAddress());
    }

    @Override
    public Optional<GetAllId> getAccountIdCustomerIdUserId(Long userId) {
        Optional<Account> account = accountRepository.findByUserId(userId);
        if (account.isPresent()) {
            ResponseEntity<ApiResponse<GetAllId>> getAccountIdAndCustomerId = accountClient.getAllId(account.get().getId());
            if (getAccountIdAndCustomerId.getBody() != null && getAccountIdAndCustomerId.getBody().isSuccess()) {
                GetAllId data  = getAccountIdAndCustomerId.getBody().getData();
                Optional<SavingsAccount> savingsAccount = savingsAccountRepository.findByUserId(userId);
                data.setAccountId(account.get().getId());
                data.setCustomerId(data.getCustomerId());
                data.setUserId(account.get().getUser().getId());
                if (savingsAccount.isPresent()) {
                    data.setSavingAccountId(savingsAccount.get().getId());
                } else {
                    data.setSavingAccountId(null);
                }
                return Optional.of(data);
            } else {
                logger.error("Failed to fetch data from external service for account id: {}", userId);
            }
        }
        return Optional.empty();
    }

    @Override
    public AccountDto readAccountByAccountNumber(Long accountNumber) {

        return accountRepository.findAccountByAccountNumber(Long.valueOf(accountNumber))
                .map(account -> {
                    AccountDto accountDto = accountMapper.toDto(account);
                    return accountDto;
                })
                .orElseThrow(() -> new AccountNotFoundException("Account not found for account number: " + accountNumber));
    }

    @Override
    public ApiResponse updateAccount(Long accountNumber, AccountDto accountDto) {
        return accountRepository.findAccountByAccountNumber(accountDto.getAccountNumber())
                .map(account -> {
                    BeanUtils.copyProperties(accountDto, account);
                    accountRepository.save(account);
                    return ApiResponse.builder()
                            .status(200)
                            .success(true)
                            .message("Account updated successfully").build();
                }).orElseThrow(() -> new ResourceNotFound("Account not found on the server"));
    }


}
