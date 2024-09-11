package com.ctv_it.customer_service.service;

import com.ctv_it.customer_service.dto.VerificationCodeDto;
import org.springframework.stereotype.Service;

@Service
public interface VerificationCodeService {

    VerificationCodeDto generateCode(Long customerId, String email);

    boolean verifyCode(Long customerId, String code);
}

