package com.ojt.klb.external;

import com.ojt.klb.model.dto.Transaction;
import com.ojt.klb.model.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "banking-transaction-service", url= "http://localhost:8070/api/v1/transactions")
public interface TransactionClient {

    @PostMapping
    ResponseEntity<ApiResponse> handleTransaction(@RequestBody Transaction transaction);

    @PostMapping("/internal")
    ResponseEntity<ApiResponse> makeInternalTransactions(@RequestBody List<Transaction> transactions, @RequestParam String transactionReference);
}
