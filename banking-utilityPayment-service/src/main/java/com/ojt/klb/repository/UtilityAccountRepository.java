package com.ojt.klb.repository;

import com.ojt.klb.model.entity.UtilityAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UtilityAccountRepository extends JpaRepository<UtilityAccount, Long> {
    Optional<UtilityAccount> findById(Long id);
}
