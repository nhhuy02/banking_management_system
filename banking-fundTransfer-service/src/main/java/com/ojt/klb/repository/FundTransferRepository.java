package com.ojt.klb.repository;

import com.ojt.klb.model.entity.FundTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FundTransferRepository extends JpaRepository<FundTransfer, Long> {
    Optional<FundTransfer> findByTransactionReference(String referenceNumber);

    List<FundTransfer> findByFromAccount(String accountNumber);

    List<FundTransfer> findByFromAccountAndTransferredOnBetween(
            String fromAccount,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime
    );

    @Query("SELECT ft FROM FundTransfer ft WHERE ft.fromAccount = :accountNumber OR ft.toAccount = :accountNumber")
    List<FundTransfer> findByFromAccountOrToAccount(@Param("accountNumber") String accountNumber);
}
