package com.ctv_it.customer_service.controller;

import com.ctv_it.customer_service.dto.VerificationCodeRequestDto;
import com.ctv_it.customer_service.dto.VerifyCodeDto;
import com.ctv_it.customer_service.response.ApiResponse;
import com.ctv_it.customer_service.service.VerificationCodeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.function.Function;

@RestController
@RequestMapping("/api/v1/customer/verify-code")
@Validated
public class VerificationCodeController {

    private static final Logger logger = LoggerFactory.getLogger(VerificationCodeController.class);

    private final VerificationCodeService verificationCodeService;

    public VerificationCodeController(VerificationCodeService verificationCodeService) {
        this.verificationCodeService = verificationCodeService;
    }

    @PostMapping("/generate/{customerId}")
    public ResponseEntity<ApiResponse<String>> generateCode(@PathVariable Long customerId,
            @Valid @RequestBody VerificationCodeRequestDto dto) {
        logger.info("Request to generate verification code for customer ID: {} with email: {}", customerId,
                dto.getEmail());
        VerificationCodeRequestDto codeDto = verificationCodeService.generateCode(customerId, dto.getEmail());
        String successMessage = "Created code successfully, please check your email!";
        ApiResponse<String> response = new ApiResponse<>(HttpStatus.CREATED.value(), successMessage, true, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify/{customerId}")
    public ResponseEntity<ApiResponse<String>> verifyCode(
            @PathVariable Long customerId, @Valid @RequestBody VerifyCodeDto dto) {

        logger.info("Request to verify code for customer ID: {} with code: {}", customerId, dto.getCode());
        boolean isVerified = verificationCodeService.verifyCode(customerId, dto.getCode());

        if (isVerified) {
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(), "Code verified successfully", true,
                    null);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Invalid or expired code",
                    false, null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/verify-reset-password/{phoneNumber}")
    public ResponseEntity<ApiResponse<String>> verifyOtpResetPassword(
            @PathVariable String phoneNumber, @Valid @RequestBody VerifyCodeDto dto) {

        logger.info("Request to verify OTP for phone number: {} with code: {}", phoneNumber, dto.getCode());

        boolean isVerified = verificationCodeService.verifyOtpResetPassword(phoneNumber, dto.getCode());

        if (isVerified) {
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(), "OTP verified successfully", true,
                    null);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(),
                    "OTP verification failed or already verified", false, null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

}
