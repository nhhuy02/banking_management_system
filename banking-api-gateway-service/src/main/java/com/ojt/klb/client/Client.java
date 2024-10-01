package com.ojt.klb.client;

import com.ojt.klb.dto.DataForJwt;
import com.ojt.klb.dto.IdDto;
import com.ojt.klb.dto.RegisterRequest;
import com.ojt.klb.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "banking-account-service", url = "http://localhost:8080", fallback = Fallback.class)
public interface Client {
    @PostMapping("/api/v1/account/users/login")
    ResponseEntity<ApiResponse<DataForJwt>> getDataLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password);

    @GetMapping("/api/v1/account/{userId}/all-id-for-gateway")
    ResponseEntity<ApiResponse<IdDto>> getAllId(@PathVariable Long userId);

    @PostMapping("/api/v1/account/users/register")
    ResponseEntity<ApiResponse<DataForJwt>> registerData(@RequestBody RegisterRequest registerRequest);
}

