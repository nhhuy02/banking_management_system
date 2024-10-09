package com.ojt.klb.service;

import com.ojt.klb.model.TransactionStatus;
import com.ojt.klb.model.TransactionType;
import com.ojt.klb.model.dto.SearchDataDto;
import com.ojt.klb.model.dto.TransactionDto;
import com.ojt.klb.model.response.ApiResponse;
import com.ojt.klb.model.response.TransactionResponse;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {

  ApiResponse handleTransaction(TransactionDto transactionDto);

  ApiResponse saveInternalTransaction(List<TransactionDto> transactionDtos, String referenceNumber);

  ApiResponse saveExternalTransaction(List<TransactionDto> transactionDtos, String referenceNumber);

  List<TransactionResponse> getTransaction(String accountId);

  List<TransactionResponse> getTransactionByTransactionReference(String transactionReference);

  SearchDataDto findLastTransactionByAccountNumberBeforeDate(String accountNumber,
      LocalDate dateBefore);

  List<SearchDataDto> findTransactions(String accountNumber, TransactionType transactionType,
      LocalDate fromDate, LocalDate toDate, TransactionStatus status);

  ApiResponse saveUtilityPaymentTransaction(TransactionDto transactionDto, String referenceNumber);
}
