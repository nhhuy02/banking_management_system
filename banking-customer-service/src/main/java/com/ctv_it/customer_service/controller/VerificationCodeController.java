package com.ctv_it.customer_service.controller;

import com.ctv_it.customer_service.dto.VerificationCodeDto;
import com.ctv_it.customer_service.response.ApiResponse;
import com.ctv_it.customer_service.service.VerificationCodeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer/verificode")
@Validated
public class VerificationCodeController {

    private static final Logger logger = LoggerFactory.getLogger(VerificationCodeController.class);

    @Autowired
    private VerificationCodeService verificationCodeService;

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<String>> generateCode(@Valid @RequestBody VerificationCodeDto dto) {
        logger.info("Request to generate verification code for customer ID: {} with email: {}", dto.getCustomerId(), dto.getEmail());
        VerificationCodeDto codeDto = verificationCodeService.generateCode(dto.getCustomerId(), dto.getEmail());
        String successMessage = "Created code successfully, please check your email!";
        ApiResponse<String> response = new ApiResponse<>(HttpStatus.CREATED.value(), successMessage, true, null);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verifyCode(@Valid @RequestBody VerificationCodeDto dto) {
        logger.info("Request to verify code for customer ID: {} with code: {}", dto.getCustomerId(), dto.getCode());
        boolean isVerified = verificationCodeService.verifyCode(dto.getCustomerId(), dto.getCode());

        if (isVerified) {
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(), "Code verified successfully", true, null);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Invalid or expired code", false, null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}