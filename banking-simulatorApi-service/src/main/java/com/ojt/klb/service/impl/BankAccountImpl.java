package com.ojt.klb.service.impl;

import com.ojt.klb.exception.AccountNotFoundException;
import com.ojt.klb.exception.GlobalErrorCode;
import com.ojt.klb.exception.ResourceNotFound;
import com.ojt.klb.model.dto.BankAccountDto;
import com.ojt.klb.model.entity.BankAccount;
import com.ojt.klb.model.mapper.BankAccountMapper;
import com.ojt.klb.repository.BankAccountRepository;
import com.ojt.klb.response.ApiResponse;
import com.ojt.klb.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BankAccountImpl implements BankAccountService {
    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper = new BankAccountMapper();

    @Override
    public BankAccount createBankAccount(BankAccountDto bankAccountDto) {
        BankAccount bankAccount = new BankAccount();
        bankAccount = bankAccountMapper.convertToEntity(bankAccountDto);
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public BankAccountDto findByAccountNumber(String accountNumber) {
        return bankAccountRepository.findByAccountNumber(accountNumber)
                .map(bankAccount -> {
                    BankAccountDto bankAccountDto = bankAccountMapper.convertToDto(bankAccount);
                    return bankAccountDto;
                })
                .orElseThrow(() -> new AccountNotFoundException("Account not found on the server", GlobalErrorCode.NOT_FOUND));
    }

    @Override
    public String getBankName(String accountNumber) {
        return bankAccountRepository.findByAccountNumber(accountNumber)
                .map(bankAccount -> bankAccount.getBankName())
                .orElseThrow(() -> new ResourceNotFound("Account not found on the server", GlobalErrorCode.NOT_FOUND));
    }

    @Override
    public ApiResponse updateAccount(String accountNumber, BankAccountDto bankAccountDto) {

        return bankAccountRepository.findByAccountNumber(bankAccountDto.getAccountNumber())
                .map(bankAccount -> {
                    BeanUtils.copyProperties(bankAccountDto, bankAccount);
                    bankAccountRepository.save(bankAccount);
                    return ApiResponse.builder()
                            .status(200)
                            .message("Account updated successfully").build();
                }).orElseThrow(() -> new ResourceNotFound(GlobalErrorCode.NOT_FOUND, "Account not found on the server"));
    }

    @Override
    public BigDecimal getBalance(String accountNumber) {
        return bankAccountRepository.findByAccountNumber(accountNumber)
                .map(account -> account.getBalance())
                .orElseThrow(() -> new ResourceNotFound(GlobalErrorCode.NOT_FOUND,"Account not found on the server"));
    }

    @Override
    public boolean validateAccount(String accountNumber, String bankName) {
        return bankAccountRepository.findByAccountNumber(accountNumber)
                .map(account -> account.getAccountNumber().equals(accountNumber)
                        && account.getBankName().equals(bankName))
                .orElse(false);
    }

}
