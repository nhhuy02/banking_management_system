package com.ctv_it.customer_service.client;

import com.ctv_it.customer_service.dto.AccountData;
import com.ctv_it.customer_service.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "banking-account-service", url = "http://localhost:8080", fallback = AccountFallback.class)
public interface AccountClient {

    @GetMapping("/api/v1/account/{accountId}")
    ResponseEntity<ApiResponse<AccountData>> getAccountData(@PathVariable("accountId") Long accountId);
}

