package com.ojt.klb.controller;

import com.ojt.klb.dto.AccountDto;
import com.ojt.klb.dto.ChangeStatusDto;
import com.ojt.klb.dto.GetAllId;
import com.ojt.klb.response.ApiResponse;
import com.ojt.klb.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);


    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse<AccountDto>> getAccountById(@PathVariable Long accountId) {
        Optional<AccountDto> accountDtoOptional = accountService.getAccountById(accountId);

        if (accountDtoOptional.isPresent()) {
            ApiResponse<AccountDto> response = new ApiResponse<>(HttpStatus.OK.value(), "Account data fetched successfully", true, accountDtoOptional.get()
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<AccountDto> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "Account not found or suspended", false, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/{accountId}/status")
    public ResponseEntity<ApiResponse<String>> changeStatusAccount(
            @PathVariable("accountId") Long accountId,
            @RequestBody ChangeStatusDto changeStatusDto) {
        try {
            accountService.changeStatusAccount(accountId, changeStatusDto);
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(), "Account status updated successfully", true, null);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            logger.error("Error updating account status: {}", e.getMessage());
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error updating account status", false, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/data/{accountNumber}")
    public ResponseEntity<ApiResponse<AccountDto>> getDataAccountNumber(@PathVariable String accountNumber) {
        Optional<Long> accountIdOptional = accountService.getAccountIdByAccountNumber(accountNumber);

        if (accountIdOptional.isPresent()) {
            Long accountId = accountIdOptional.get();
            Optional<AccountDto> accountDataOptional = accountService.getDataByAccountNumber(accountId);

            if (accountDataOptional.isPresent()) {
                AccountDto accountData = accountDataOptional.get();
                ApiResponse<AccountDto> response = new ApiResponse<>(HttpStatus.OK.value(), "Account found", true, accountData);
                return ResponseEntity.ok(response);
            } else {
                ApiResponse<AccountDto> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Account data not found", false, null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } else {
            ApiResponse<AccountDto> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "Account not found", false, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }


    @GetMapping("/{userId}/all-id-for-gateway")
    public ResponseEntity<ApiResponse<GetAllId>> getAllIdForApiGateWay(@PathVariable Long userId) {
        Optional<GetAllId> data = accountService.getAccountIdCustomerIdUserId(userId);
        if (data.isPresent()) {
            ApiResponse<GetAllId> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "All ID data fetched successfully",
                    true,
                    data.get()
            );
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<GetAllId> response = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "All id not found",
                    false,
                    null
            );
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping
    public ResponseEntity<ApiResponse> updateAccount(@RequestParam String accountNumber, @RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(accountService.updateAccount(accountNumber, accountDto));
    }

    @GetMapping("/balance")
    public ResponseEntity<String> accountBalance(@RequestParam String accountNumber) {
        return ResponseEntity.ok(accountService.getBalance(accountNumber));
    }
}

