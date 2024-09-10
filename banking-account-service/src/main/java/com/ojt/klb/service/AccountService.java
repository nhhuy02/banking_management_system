package com.ojt.klb.service;

import java.util.List;

import com.ojt.klb.model.Account;
import com.ojt.klb.request.AccountRequest;

public interface AccountService {
    Account createAccount(AccountRequest request);

    Account updateAccount(Long accountId, AccountRequest request);

    void closeAccount(Long accountId);

    Account getAccountById(Long accountId);

    List<Account> getAllAccounts();
}
