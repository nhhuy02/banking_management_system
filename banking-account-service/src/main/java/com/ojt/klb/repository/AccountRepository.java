package com.ojt.klb.repository;

import com.ojt.klb.model.AccountType;
import com.ojt.klb.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String cardNumber);
    Optional<Account> findAccountByUserIdAndAccountType(Long userId, AccountType accountType);
    Optional<Account> findAccountByAccountNumber(String accountNumber);
    Optional<Account> findAccountByUserId(Long userId);
}
