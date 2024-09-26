package com.ctv_it.customer_service.client;

import com.ctv_it.customer_service.dto.AccountData;
import com.ctv_it.customer_service.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public class AccountFallback implements AccountClient {

    @Override
    public ResponseEntity<ApiResponse<AccountData>> getAccountData(Long accountId) {
        return null;
    }
}
