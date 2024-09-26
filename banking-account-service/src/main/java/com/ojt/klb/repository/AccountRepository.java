package com.ojt.klb.repository;

import com.ojt.klb.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByAccountNumber(String cardNumber);

//    Optional<Account> findAccountByUserIdAndAccountType(Long userId, AccountType accountType);

    Optional<Account> findAccountByAccountNumber(String accountNumber);

//    List<Account> findAccountsByUserId(Long userId);

    long deleteByAccountNumber(String accountNumber);
}
