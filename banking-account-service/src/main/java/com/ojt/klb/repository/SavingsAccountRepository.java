package com.ojt.klb.repository;

import com.ojt.klb.model.SavingsAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SavingsAccountRepository extends JpaRepository<SavingsAccount, Long> {
    boolean existsByUserId(Long userId);

    Optional<SavingsAccount> findByUserId(Long userId);
}
