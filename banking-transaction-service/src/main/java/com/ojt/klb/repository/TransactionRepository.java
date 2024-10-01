package com.ojt.klb.repository;

import com.ojt.klb.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountNumber(Long accountNumber);
    List<Transaction> findByReferenceNumber(String referenceNumber);
}
