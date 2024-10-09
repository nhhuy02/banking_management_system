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
    public ResponseEntity<ApiResponse<String>> generateCode(@PathVariable Long customerId, @Valid @RequestBody VerificationCodeRequestDto dto) {
        logger.info("Request to generate verification code for customer ID: {} with email: {}", customerId, dto.getEmail());
        VerificationCodeRequestDto codeDto = verificationCodeService.generateCode(customerId, dto.getEmail());
        String successMessage = "Created code successfully, please check your email!";
        ApiResponse<String> response = new ApiResponse<>(HttpStatus.CREATED.value(), successMessage, true, null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify/{customerId}")
    public ResponseEntity<ApiResponse<String>> verifyCode(
            @PathVariable Long customerId, @Valid @RequestBody VerifyCodeDto dto) {

        return handleVerification(
                customerId,
                dto.getCode(),
                (id) -> verificationCodeService.verifyCode(id, dto.getCode()),
                "Code verified successfully",
                "Invalid or expired code"
        );
    }

    @PostMapping("/verify-reset-password/{customerId}")
    public ResponseEntity<ApiResponse<String>> verifyOtpResetPassword(
            @PathVariable Long customerId, @Valid @RequestBody VerifyCodeDto dto) {

        return handleVerification(
                customerId,
                dto.getCode(),
                (id) -> verificationCodeService.verifyOtpResetPassword(id, dto.getCode()),
                "Reset password code verified successfully",
                "Invalid or expired reset password code"
        );
    }

    private ResponseEntity<ApiResponse<String>> handleVerification(
            Long customerId, String code, Function<Long, Boolean> verificationFunction,
            String successMessage, String failureMessage) {

        logger.info("Request to verify code for customer ID: {} with code: {}", customerId, code);
        boolean isVerified = verificationFunction.apply(customerId);

        if (isVerified) {
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(), successMessage, true, null);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), failureMessage, false, null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
