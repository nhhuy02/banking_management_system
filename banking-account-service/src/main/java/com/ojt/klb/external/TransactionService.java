package com.ojt.klb.external;

import com.ojt.klb.model.dto.external.TransactionResponse;
import org.springframework.cloud.openfeign.FeignClient;
//import com.ojt.klb.configuration.FeignConfiguration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "banking-transaction-service", url = "http://localhost:8070/api/v1/transactions")
public interface TransactionService {

    @GetMapping
    List<TransactionResponse> getTransactionsFromAccountNumber(@RequestParam String accountNumber);
}
