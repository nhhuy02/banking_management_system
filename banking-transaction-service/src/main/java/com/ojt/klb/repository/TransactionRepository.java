package com.ojt.klb.repository;

import com.ojt.klb.model.entity.Transaction;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransactionRepository extends JpaRepository<Transaction, Long>,
    JpaSpecificationExecutor<Transaction> {

  List<Transaction> findByAccountNumber(String accountNumber);

  List<Transaction> findByReferenceNumber(String referenceNumber);


  Transaction findTopByAccountNumberAndTransactionDateBeforeOrderByTransactionDateDesc(
      String accountNumber, LocalDateTime dateBefore);
}
