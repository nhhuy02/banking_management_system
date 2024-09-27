package com.ojt.klb.controller;

import com.ojt.klb.dto.SavingsAccountResponseDto;
import com.ojt.klb.response.ApiResponse;
import com.ojt.klb.service.SavingsAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/savings-accounts")
public class SavingsAccountController {

    private static final Logger logger = LoggerFactory.getLogger(SavingsAccountController.class);

    private final SavingsAccountService savingsAccountService;

    public SavingsAccountController(SavingsAccountService savingsAccountService) {
        this.savingsAccountService = savingsAccountService;
    }

    @PostMapping("/register/{userId}")
    public ResponseEntity<ApiResponse<Void>> createSavingsAccount(@PathVariable Long userId) {
        logger.info("Create savings account for user {}", userId);
        try {
            savingsAccountService.createSavingsAccount(userId);
            ApiResponse<Void> response = new ApiResponse<>(
                    HttpStatus.CREATED.value(),
                    "Savings account created successfully.",
                    true,
                    null
            );
            logger.info("Create savings account for user {} success", userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            logger.warn("Create savings account failed: {}", e.getMessage());
            ApiResponse<Void> response = new ApiResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    e.getMessage(),
                    false,
                    null
            );
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            logger.error("Create savings account failed: ", e);
            ApiResponse<Void> response = new ApiResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "An unexpected error occurred.",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/information/{savingAccountId}")
    public ResponseEntity<ApiResponse<SavingsAccountResponseDto>> getSavingsAccount(@PathVariable Long savingAccountId) {
        logger.info("Fetch savings account with ID {}", savingAccountId);
        Optional<SavingsAccountResponseDto> accountDto = savingsAccountService.findBySavingAccountId(savingAccountId);

        if (accountDto.isPresent()) {
            ApiResponse<SavingsAccountResponseDto> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Savings account fetched successfully.",
                    true,
                    accountDto.get()
            );
            logger.info("Fetched savings account with ID {} successfully", savingAccountId);
            return ResponseEntity.ok(response);
        } else {
            logger.warn("Savings account with ID {} not found", savingAccountId);
            ApiResponse<SavingsAccountResponseDto> response = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "Savings account not found.",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}
