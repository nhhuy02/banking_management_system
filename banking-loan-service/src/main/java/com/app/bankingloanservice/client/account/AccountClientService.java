package com.app.bankingloanservice.client.account;

import com.app.bankingloanservice.client.account.dto.AccountDto;
import com.app.bankingloanservice.client.account.dto.ApiResponse;
import com.app.bankingloanservice.exception.AccountServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountClientService {

    private final AccountClient accountClient;

    public AccountDto getAccountInfoById(Long accountId) {
        log.info("Calling Account Service to retrieve information for Account ID: {}", accountId);
        try {
            ApiResponse<AccountDto> apiResponse = accountClient.getAccountById(accountId);
            if (!apiResponse.isSuccess()) {
                log.error("Failed to retrieve account information for Account ID: {}. Error message: {}", accountId, apiResponse.getMessage());
                throw new AccountServiceException("Unable to retrieve account information from Account Service: " + apiResponse.getMessage());
            }
            log.info("Successfully retrieved account information for Account ID: {}", accountId);
            return apiResponse.getData();
        } catch (Exception e) {
            log.error("Error occurred while calling Account Service for Account ID: {}", accountId, e);
            throw new AccountServiceException("Error calling Account Service for Account ID: " + accountId, e);
        }
    }
}

