package com.ctv_it.customer_service.service;

import com.ctv_it.customer_service.dto.VerificationCodeRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface VerificationCodeService {

    VerificationCodeRequestDto generateCode(Long customerId, String email);

    boolean verifyCode(Long customerId, String code);
    boolean verifyOtpResetPassword(String phoneNumber, String code);
}

