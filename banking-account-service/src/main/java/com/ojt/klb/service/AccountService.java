package com.ojt.klb.service;

import com.ojt.klb.model.dto.AccountDto;
import com.ojt.klb.model.dto.AccountStatusUpdate;
import com.ojt.klb.model.dto.external.TransactionResponse;
import com.ojt.klb.model.dto.response.Response;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    Long createAccount(AccountDto accountDto);

    void updateStatus(String accountNumber, AccountStatusUpdate accountStatusUpdate);

    AccountDto readAccountByAccountNumber(String accountNumber);

    void updateAccount(String accountNumber, AccountDto accountDto);

    String getBalance(String accountNumber);

    List<TransactionResponse> getTransactionsFromAccountNumber(String accountNumber);

    void closeAccount(String accountNumber);

//    List<AccountDto> readAccountsByUserId(Long userId);

    void updateBalance(String accountNumber, BigDecimal amount);

    List<AccountDto> readAllAccounts();

    void deleteAccount(String accountNumber);

}
