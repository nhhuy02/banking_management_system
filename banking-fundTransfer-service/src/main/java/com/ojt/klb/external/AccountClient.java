package com.ojt.klb.external;

import com.ojt.klb.model.dto.Account;
import com.ojt.klb.model.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "banking-account-service", url = "http://localhost:8080/api/v1/account")
public interface AccountClient {
    @GetMapping
    ResponseEntity<Account> readByAccountNumber(@RequestParam Long accountNumber);

    @PutMapping
    ResponseEntity<ApiResponse> updateAccount(@RequestParam Long accountNumber, @RequestBody Account account);

}
