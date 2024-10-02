package com.app.bankingloanservice.client.account;

import com.app.bankingloanservice.client.account.dto.AccountDto;
import com.app.bankingloanservice.client.account.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "banking-account-service", url = "${app.account-client.url}")
public interface AccountClient {

    @GetMapping("/{accountId}")
    ApiResponse<AccountDto> getAccountById(@PathVariable("accountId") Long accountId);

}

