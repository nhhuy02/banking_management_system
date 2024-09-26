package com.ojt.klb.repository;

import com.ojt.klb.model.entity.FundTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FundTransferRepository extends JpaRepository<FundTransfer, Long> {
    Optional<FundTransfer> findByTransactionReference(String referenceNumber);

    List<FundTransfer> findByFromAccount(String accountNumber);
}
