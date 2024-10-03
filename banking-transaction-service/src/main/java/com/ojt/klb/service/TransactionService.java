package com.ojt.klb.service;

import com.ojt.klb.model.dto.TransactionDto;
import com.ojt.klb.model.request.TransactionRequest;
import com.ojt.klb.model.response.ApiResponse;

import java.util.List;

public interface TransactionService {
    public ApiResponse handleTransaction(TransactionDto transactionDto);
    public ApiResponse saveTransaction(List<TransactionDto> transactionDtos, String referenceNumber);
    List<TransactionRequest> getTransaction(String accountId);
    List<TransactionRequest> getTransactionByTransactionReference(String transactionReference);
}
