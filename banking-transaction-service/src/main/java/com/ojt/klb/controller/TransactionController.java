package com.ojt.klb.controller;

import com.ojt.klb.model.TransactionStatus;
import com.ojt.klb.model.TransactionType;
import com.ojt.klb.model.dto.SearchDataDto;
import com.ojt.klb.model.dto.TransactionDto;
import com.ojt.klb.model.request.UtilityPaymentRequest;
import com.ojt.klb.model.response.ApiResponse;
import com.ojt.klb.model.response.TransactionResponse;
import com.ojt.klb.service.TransactionService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

  private final TransactionService service;

  @PostMapping
  public ResponseEntity<ApiResponse> handleTransaction(
      @Valid @RequestBody TransactionDto transactionDto) {
    return new ResponseEntity<>(service.handleTransaction(transactionDto), HttpStatus.CREATED);
  }

  @PostMapping("/internal")
  public ResponseEntity<ApiResponse> saveTransaction(
      @RequestBody List<TransactionDto> transactionDtos,
      @RequestParam String transactionReference) {
    return new ResponseEntity<>(
        service.saveInternalTransaction(transactionDtos, transactionReference), HttpStatus.CREATED);
  }

  @PostMapping("/external")
  public ResponseEntity<ApiResponse> saveExternalTransaction(
      @RequestBody List<TransactionDto> transactionDtos,
      @RequestParam String transactionReference) {
    return new ResponseEntity<>(
        service.saveExternalTransaction(transactionDtos, transactionReference), HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<TransactionResponse>> getTransactions(
      @RequestParam String accountNumber) {
    return new ResponseEntity<>(service.getTransaction(accountNumber), HttpStatus.OK);
  }

  @GetMapping("/{referenceNumber}")
  public ResponseEntity<List<TransactionResponse>> getTransactionByTransactionReference(
      @PathVariable String referenceNumber) {
    return new ResponseEntity<>(service.getTransactionByTransactionReference(referenceNumber),
        HttpStatus.OK);
  }

  @GetMapping("/last")
  public ResponseEntity<?> findLastTransactionByAccountNumberBeforeDate(
      @RequestParam String accountNumber, @RequestParam LocalDate dateBefore) {

    SearchDataDto searchDataDto = service.findLastTransactionByAccountNumberBeforeDate(
        accountNumber, dateBefore);

    String messageResponse = "Search transaction completed successfully." +
        ((searchDataDto == null) ? " But no one found" : "");

    return ResponseEntity.ok(ApiResponse.builder()
        .status(200)
        .success(true)
        .message(messageResponse)
        .data(searchDataDto)
        .build());
  }

  @GetMapping("/search")
  public ResponseEntity<?> findTransactions(
      @RequestParam String accountNumber,
      @RequestParam(required = false) TransactionType transactionType,
      @RequestParam(required = false) LocalDate fromDate,
      @RequestParam(required = false) LocalDate toDate,
      @RequestParam(required = false) TransactionStatus status) {

    List<SearchDataDto> transactions = service.findTransactions(accountNumber, transactionType,
        fromDate, toDate, status);

    return ResponseEntity.ok(ApiResponse.builder()
        .status(200)
        .success(true)
        .message("Search transaction completed successfully")
        .data(transactions)
        .build());
  }

  @PostMapping("/util-payment")
  public ResponseEntity utilPayment(@RequestBody UtilityPaymentRequest utilityPaymentRequest) {
    return ResponseEntity.ok(service.utilPayment(utilityPaymentRequest));
  }
}
