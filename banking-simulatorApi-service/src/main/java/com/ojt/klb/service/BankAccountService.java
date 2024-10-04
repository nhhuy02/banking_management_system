package com.ojt.klb.service;

import com.ojt.klb.model.dto.BankAccountDto;
import com.ojt.klb.model.entity.BankAccount;
import com.ojt.klb.response.ApiResponse;

import java.math.BigDecimal;

public interface BankAccountService {
    BankAccount createBankAccount(BankAccountDto bankAccountDto);
    BankAccountDto findByAccountNumber(String accountNumber);
    String getBankName(String accountNumber);
    ApiResponse updateAccount(String accountNumber, BankAccountDto bankAccountDto);
    BigDecimal getBalance(String accountNumber);
    boolean validateAccount(String accountNumber, String bankName);

}
