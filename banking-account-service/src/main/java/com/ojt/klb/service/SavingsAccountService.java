package com.ojt.klb.service;

import com.ojt.klb.dto.SavingsAccountResponseDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface SavingsAccountService {
    Optional<SavingsAccountResponseDto> findBySavingAccountId(Long savingAccountId);
    void createSavingsAccount(Long userID);
}
