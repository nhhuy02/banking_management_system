package com.ojt.klb.client;

import com.ojt.klb.dto.AccountDto;
import com.ojt.klb.dto.GetAllId;
import com.ojt.klb.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public class AccountFallback implements AccountClient {
    @Override
    public ResponseEntity<ApiResponse<AccountDto>> getData(Long accountId) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<GetAllId>> getAllId(Long accountId) {
        return null;
    }
}
