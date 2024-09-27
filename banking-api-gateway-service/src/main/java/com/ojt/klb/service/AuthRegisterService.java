package com.ojt.klb.service;

import com.ojt.klb.dto.RegisterRequest;
import org.springframework.stereotype.Service;

@Service
public interface AuthRegisterService {
    String VerifyRegisterAndGenJWT(RegisterRequest registerRequest);
}
