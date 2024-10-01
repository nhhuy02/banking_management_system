package com.ojt.klb.controller;

import com.ojt.klb.model.dto.TransactionDto;
import com.ojt.klb.model.request.TransactionRequest;
import com.ojt.klb.model.response.ApiResponse;
import com.ojt.klb.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService service;

    @PostMapping
    public ResponseEntity<ApiResponse> handleTransaction(@Valid @RequestBody TransactionDto transactionDto) {
        return new ResponseEntity<>(service.handleTransaction(transactionDto), HttpStatus.CREATED);
    }

    @PostMapping("/internal")
    public ResponseEntity<ApiResponse> makeInternalTransaction(@RequestBody List<TransactionDto> transactionDtos, @RequestParam String transactionReference) {
        return new ResponseEntity<>(service.internalTransaction(transactionDtos, transactionReference), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransactionRequest>> getTransactions(@RequestParam String accountNumber) {
        return new ResponseEntity<>(service.getTransaction(accountNumber), HttpStatus.OK);
    }

    @GetMapping("/{referenceNumber}")
    public ResponseEntity<List<TransactionRequest>> getTransactionByTransactionReference(@PathVariable String referenceNumber) {
        return new ResponseEntity<>(service.getTransactionByTransactionReference(referenceNumber), HttpStatus.OK);
    }
}
