package com.ojt.klb.repository;

import com.ojt.klb.model.TransactionStatus;
import com.ojt.klb.model.TransactionType;
import com.ojt.klb.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountNumber(String accountNumber);
    List<Transaction> findByReferenceNumber(String referenceNumber);
    List<Transaction> findByTransactionTypeAndTransactionDateBetweenAndStatus(
            TransactionType transactionType,
            LocalDateTime fromDate,
            LocalDateTime toDate,
            TransactionStatus status
    );
}
