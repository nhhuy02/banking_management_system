package com.ojt.klb.service;

import com.ojt.klb.dto.AccountDto;
import com.ojt.klb.dto.ChangeStatusDto;
import com.ojt.klb.dto.GetAllId;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public interface AccountService {
    Optional<AccountDto> getAccountById(Long id);
    void changeStatusAccount(Long id , ChangeStatusDto changeStatusDto);
    Optional<Long> getAccountIdByAccountNumber(String accountNumber);
    Optional<AccountDto> getDataByAccountNumber(Long accountId);
    Optional<GetAllId> getAccountIdCustomerIdUserId(Long userId);
}
