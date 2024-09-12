package com.ojt.klb.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ojt.klb.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByAccountNumber(String accountNumber);
}
