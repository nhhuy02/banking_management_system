package com.ojt.klb.client;

import com.ojt.klb.dto.AccountDto;
import com.ojt.klb.dto.GetAllId;
import com.ojt.klb.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "banking-customer-service", url = "http://localhost:8082", fallback = AccountFallback.class)
public interface AccountClient {
    @GetMapping("/api/v1/customer/{accountId}")
    ResponseEntity<ApiResponse<AccountDto>> getData(@PathVariable("accountId") Long accountId);

    @GetMapping("/api/v1/customer/{accountId}/customer-info-for-account-service")
    ResponseEntity<ApiResponse<GetAllId>> getAllId(@PathVariable("accountId") Long accountId);
}
