package com.ojt.klb.client;

import com.ojt.klb.dto.DataForJwt;
import com.ojt.klb.dto.IdDto;
import com.ojt.klb.dto.RegisterRequest;
import com.ojt.klb.response.ApiResponse;
import org.springframework.http.ResponseEntity;

public class Fallback implements Client {
    @Override
    public ResponseEntity<ApiResponse<DataForJwt>> getDataLogin(String username, String password) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<IdDto>> getAllId(Long userId) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse<DataForJwt>> registerData(RegisterRequest registerRequest) {
        return null;
    }
}
