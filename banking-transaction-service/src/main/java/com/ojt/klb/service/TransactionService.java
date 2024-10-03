package com.ojt.klb.service;

import com.ojt.klb.model.TransactionStatus;
import com.ojt.klb.model.TransactionType;
import com.ojt.klb.model.dto.TransactionDto;
import com.ojt.klb.model.request.TransactionRequest;
import com.ojt.klb.model.response.ApiResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    public ApiResponse handleTransaction(TransactionDto transactionDto);
    public ApiResponse saveTransaction(List<TransactionDto> transactionDtos, String referenceNumber);
    List<TransactionRequest> getTransaction(String accountId);
    List<TransactionRequest> getTransactionByTransactionReference(String transactionReference);
    List<TransactionDto> findTransactions(TransactionType transactionType, LocalDateTime fromDate, LocalDateTime toDate, TransactionStatus status);
}
