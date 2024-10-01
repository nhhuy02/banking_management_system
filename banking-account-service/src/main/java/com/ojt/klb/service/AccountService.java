package com.ojt.klb.service;

import com.ojt.klb.dto.AccountDto;
import com.ojt.klb.dto.ChangeStatusDto;
import com.ojt.klb.dto.GetAllId;
import com.ojt.klb.response.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public interface AccountService {
    Optional<AccountDto> getAccountById(Long id);
    void changeStatusAccount(Long id , ChangeStatusDto changeStatusDto);
    Optional<Long> getAccountIdByAccountNumber(String accountNumber);
    String getFullNameByAccountId(Long accountId);
    Optional<GetAllId> getAccountIdCustomerIdUserId(Long userId);
    AccountDto readAccountByAccountNumber(Long accountNumber);
    ApiResponse updateAccount(Long accountNumber, AccountDto accountDto);
}
