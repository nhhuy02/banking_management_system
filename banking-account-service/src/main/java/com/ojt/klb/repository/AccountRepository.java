package com.ojt.klb.repository;

import com.ojt.klb.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String accountNumber);

    Optional<Account> findAccountByAccountNumber(String accountNumber);

    Optional<Account> findByUserId(Long userId);
}
