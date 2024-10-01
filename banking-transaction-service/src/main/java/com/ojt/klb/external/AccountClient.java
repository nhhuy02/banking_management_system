package com.ojt.klb.external;

import com.ojt.klb.model.external.Account;
import com.ojt.klb.model.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "banking-account-service", url = "http://localhost:8080/api/v1/account")
public interface AccountClient {
    @GetMapping
    ResponseEntity<Account> readByAccountNumber(@RequestParam Long accountNumber);

    @PutMapping
    ResponseEntity<Response> updateAccount(@RequestParam Long accountNumber, @RequestBody Account account);
}