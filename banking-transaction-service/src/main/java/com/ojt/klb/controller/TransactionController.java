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
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService service;

    @PostMapping
    public ResponseEntity<ApiResponse> handleTransaction(@Valid @RequestBody TransactionDto transactionDto) {
        ApiResponse response = service.handleTransaction(transactionDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/internal")
    public ResponseEntity<ApiResponse> saveTransaction(@RequestBody List<TransactionDto> transactionDtos,
            @RequestParam String transactionReference) {
        ApiResponse response = service.saveInternalTransaction(transactionDtos, transactionReference);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/external")
    public ResponseEntity<ApiResponse> saveExternalTransaction(@RequestBody List<TransactionDto> transactionDtos,
            @RequestParam String transactionReference) {
        ApiResponse response = service.saveExternalTransaction(transactionDtos, transactionReference);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactions(@RequestParam String accountNumber) {
        List<TransactionResponse> transactions = service.getTransaction(accountNumber);
        ApiResponse<List<TransactionResponse>> response = new ApiResponse<>(HttpStatus.OK.value(),
                "Transactions retrieved successfully", true, transactions);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{referenceNumber}")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getTransactionByTransactionReference(
            @PathVariable String referenceNumber) {
        List<TransactionResponse> transactions = service.getTransactionByTransactionReference(referenceNumber);
        ApiResponse<List<TransactionResponse>> response = new ApiResponse<>(HttpStatus.OK.value(),
                "Transaction retrieved successfully", true, transactions);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<SearchDataDto>>> findTransactions(
            @RequestParam String accountNumber,
            @RequestParam(required = false) TransactionType transactionType,
            @RequestParam(required = false) LocalDate fromDate,
            @RequestParam(required = false) LocalDate toDate,
            @RequestParam(required = false) TransactionStatus status) {

        List<SearchDataDto> transactions = service.findTransactions(accountNumber, transactionType, fromDate, toDate,
                status);
        ApiResponse<List<SearchDataDto>> response = new ApiResponse<>(HttpStatus.OK.value(), "Transactions found", true,
                transactions);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/util-payment")
    public ResponseEntity utilPayment(@RequestBody UtilityPaymentRequest utilityPaymentRequest) {
        return ResponseEntity.ok(service.utilPayment(utilityPaymentRequest));
    }
}
