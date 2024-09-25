package com.ojt.klb.service;

import com.ojt.klb.model.dto.TransactionDto;
import com.ojt.klb.model.request.TransactionRequest;
import com.ojt.klb.model.response.Response;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionService {
    public Response handleTransaction(TransactionDto transactionDto);
    public Response internalTransaction(List<TransactionDto> transactionDtos, String referenceNumber);
    List<TransactionRequest> getTransaction(String accountId);
    List<TransactionRequest> getTransactionByTransactionReference(String transactionReference);
}
