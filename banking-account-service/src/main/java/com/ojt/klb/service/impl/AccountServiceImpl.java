package com.ojt.klb.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ojt.klb.model.Account;
import com.ojt.klb.repository.AccountRepository;
import com.ojt.klb.request.AccountRequest;
import com.ojt.klb.service.AccountService;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    //
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Account createAccount(AccountRequest request) {
        Account account = new Account();
        account.setCustomerId(request.getCustomerId());
        account.setType(request.getAccountType());
        return accountRepository.save(account);
    }

    @Override
    public Account updateAccount(Long accountId, AccountRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setType(request.getAccountType());
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
}
