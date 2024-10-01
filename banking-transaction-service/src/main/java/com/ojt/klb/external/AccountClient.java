package com.ojt.klb.external;

import com.ojt.klb.model.external.Account;
import com.ojt.klb.model.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "banking-account-service", url = "http://localhost:8080/api/v1/account")
public interface AccountClient {
    @GetMapping("/data/{accountNumber}")
    ResponseEntity<ApiResponse<Account>> getDataAccountNumber(@PathVariable String accountNumber);

    @PutMapping
    ResponseEntity<ApiResponse> updateAccount(@RequestParam String accountNumber, @RequestBody Account account);
}