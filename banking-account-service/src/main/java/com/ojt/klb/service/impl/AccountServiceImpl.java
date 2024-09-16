package com.ojt.klb.service.impl;

import com.ojt.klb.Utils.AccountUtils;
import com.ojt.klb.exception.*;
import com.ojt.klb.external.TransactionService;
// import com.ojt.klb.external.UserService;
import com.ojt.klb.model.AccountStatus;
import com.ojt.klb.model.AccountType;
import com.ojt.klb.model.dto.AccountDto;
import com.ojt.klb.model.dto.AccountStatusUpdate;
import com.ojt.klb.model.dto.external.TransactionResponse;
// import com.ojt.klb.model.dto.external.UserDto;
import com.ojt.klb.model.dto.response.Response;
import com.ojt.klb.model.entity.Account;
import com.ojt.klb.repository.AccountRepository;
import com.ojt.klb.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
// import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
// import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    // private final UserService userService;
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    @Override
    public Long createAccount(AccountDto accountDto) {

        // ResponseEntity<UserDto> user =
        // userService.readUserById(accountDto.getUserId());
        // if (Objects.isNull(user.getBody())) {
        // throw new ResourceNotFound("User not found on the server");
        // }

        accountRepository
                .findAccountByUserIdAndAccountType(accountDto.getUserId(),
                        AccountType.valueOf(accountDto.getAccountType()))
                .ifPresent(account -> {
                    log.error("Account already exists on the server");
                    throw new ResourceConflict("Account already exists on the server");
                });
        Account account = Account.builder()
                .accountNumber(generateUniqueAccountNumber())
                .accountStatus(AccountStatus.PENDING)
                .availableBalance(BigDecimal.ZERO)
                .accountType(AccountType.valueOf(accountDto.getAccountType()))
                .userId(accountDto.getUserId())
                .build();

        accountRepository.save(account);

        return account.getAccountId();
    }

    public String generateUniqueAccountNumber() {
        String accountNumber = AccountUtils.generateAccountNumber();
        while (accountRepository.existsByAccountNumber(accountNumber)) {
            accountNumber = AccountUtils.generateAccountNumber();
        }

        return accountNumber;
    }

    @Override
    public void updateStatus(String accountNumber, AccountStatusUpdate accountStatusUpdate) {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFound("Account not found on the server"));

        if (!account.getAccountStatus().equals(AccountStatus.ACTIVE)) {
            throw new AccountStatusException("Account is inactive/closed");
        }

        account.setAccountStatus(accountStatusUpdate.getAccountStatus());
        accountRepository.save(account);

        log.info("Account status updated for account number {}: new status {}", accountNumber,
                account.getAccountStatus());
    }

    @Override
    public AccountDto readAccountByAccountNumber(String accountNumber) {
        return accountRepository.findAccountByAccountNumber(accountNumber)
                .map(account -> {
                    return AccountDto.builder()
                            .accountId(account.getAccountId())
                            .accountNumber(account.getAccountNumber())
                            .accountType(String.valueOf(account.getAccountType()))
                            .accountStatus(String.valueOf(account.getAccountStatus()))
                            .availableBalance(account.getAvailableBalance())
                            .userId(account.getUserId())
                            .build();
                })
                .orElseThrow(ResourceNotFound::new);
    }

    @Override
    public void updateAccount(String accountNumber, AccountDto accountDto) {
        accountRepository.findAccountByAccountNumber(accountNumber)
                .map(account -> {
                    BeanUtils.copyProperties(accountDto, account);
                    return accountRepository.save(account);
                })
                .orElseThrow(() -> new ResourceNotFound("Account not found on the server"));
    }

    @Override
    public String getBalance(String accountNumber) {
        return accountRepository.findAccountByAccountNumber(accountNumber)
                .map(account -> account.getAvailableBalance().toString())
                .orElseThrow(ResourceNotFound::new);
    }

    @Override
    public List<TransactionResponse> getTransactionsFromAccountId(String accountId) {
        return transactionService.getTransactionsFromAccountId(accountId);
    }

    @Override
    public Response closeAccount(String accountNumber) {
        return accountRepository.findAccountByAccountNumber(accountNumber)
                .map(account -> {
                    if (BigDecimal.valueOf(Double.parseDouble(getBalance(accountNumber)))
                            .compareTo(BigDecimal.ZERO) != 0) {
                        throw new AccountClosingException("Balance should be zero");
                    }
                    account.setAccountStatus(AccountStatus.CLOSED);
                    return Response.builder()
                            .message("Account closed successfully").status(200)
                            .build();
                })
                .orElseThrow(ResourceNotFound::new);
    }

    @Override
    public List<AccountDto> readAccountsByUserId(Long userId) {
        return accountRepository.findAccountsByUserId(userId)
                .stream()
                .map(account -> AccountDto.builder()
                        .accountId(account.getAccountId())
                        .accountNumber(account.getAccountNumber())
                        .accountType(account.getAccountType().toString())
                        .accountStatus(account.getAccountStatus().toString())
                        .availableBalance(account.getAvailableBalance())
                        .userId(account.getUserId())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void updateBalance(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findAccountByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFound("Account not found: " + accountNumber));
        BigDecimal newBalance = account.getAvailableBalance().add(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InSufficientFunds("Insufficient balance for account: " + accountNumber);
        }
        account.setAvailableBalance(newBalance);
        accountRepository.save(account);
    }

}
