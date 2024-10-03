package com.ojt.klb.repository;

import com.ojt.klb.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long>, JpaSpecificationExecutor<Transaction> {
    List<Transaction> findByAccountNumber(String accountNumber);
    List<Transaction> findByReferenceNumber(String referenceNumber);


}
