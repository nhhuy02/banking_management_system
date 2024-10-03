package com.ojt.klb.external;

import com.ojt.klb.model.dto.BankAccount;
import com.ojt.klb.model.response.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Optional;

@FeignClient(name = "banking-simulatorApi-service", url = "http://localhost:8020/api/v1/bankAccounts")
public interface SimulatorApiClient {
    @GetMapping
    ResponseEntity<BankAccount> readAccountByAccountNumber(@RequestParam String accountNumber);

    @GetMapping("/bankName")
    ResponseEntity<String> getBankName(@RequestParam String accountNumber);

    @PutMapping
    ResponseEntity<ApiResponse> updateAccount(@RequestParam String accountNumber, @RequestBody BankAccount bankAccount);

    @GetMapping("/balance")
    ResponseEntity<BigDecimal> accountBalance(@RequestParam String accountNumber);

    @GetMapping("/validate-account")
    public ResponseEntity<Boolean> validateAccount(@RequestParam String accountNumber, @RequestParam String bankName);
}
