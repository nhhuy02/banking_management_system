package com.ojt.klb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ojt.klb.model.Account;
import com.ojt.klb.repository.AccountRepository;
import com.ojt.klb.request.AccountRequest;
import com.ojt.klb.service.AccountService;

import java.util.List;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    private static final Random RANDOM = new Random();
    private static final int ACCOUNT_NUMBER_LENGTH = 7;

    @Override
    public Account createAccount(AccountRequest request) {
        Account account = new Account();
        account.setCustomerId(request.getCustomerId());
        account.setType(request.getType());
        account.setStatus("active");

        // Generate a unique 7-digit account number
        String accountNumber;
        do {
            accountNumber = generateAccountNumber();
        } while (accountRepository.existsByAccountNumber(accountNumber)); // Ensure uniqueness

        account.setAccountNumber(accountNumber);
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(Long accountId, AccountRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setStatus(request.getStatus());
        return accountRepository.save(account);
    }

    @Override
    public void closeAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setStatus("closed");
        accountRepository.save(account);
    }

    @Override
    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    private String generateAccountNumber() {
        int number = RANDOM.nextInt((int) Math.pow(10, ACCOUNT_NUMBER_LENGTH - 1));
        return String.format("%0" + ACCOUNT_NUMBER_LENGTH + "d", number);
    }
}
