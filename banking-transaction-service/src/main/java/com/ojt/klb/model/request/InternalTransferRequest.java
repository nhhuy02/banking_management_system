package com.ojt.klb.model.request;

import com.ojt.klb.model.TransactionStatus;
import com.ojt.klb.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InternalTransferRequest {

    private Long accountId;

    private String fromAccountNumber;

    private String toAccountNumber;

    private TransactionType transactionType;

    private BigDecimal amount;

    private LocalDateTime localDateTime;

    private TransactionStatus status;

    private String description;
}
