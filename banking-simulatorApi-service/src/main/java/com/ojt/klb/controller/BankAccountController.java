package com.ojt.klb.controller;

import com.ojt.klb.model.dto.BankAccountDto;
import com.ojt.klb.model.entity.BankAccount;
import com.ojt.klb.response.ApiResponse;
import com.ojt.klb.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/api/v1/bankAccounts")
@RequiredArgsConstructor
public class BankAccountController {
    private final BankAccountService bankAccountService;

    @PostMapping
    ResponseEntity<BankAccount> createBankAccount(@RequestBody BankAccountDto bankAccountDto){
        return new ResponseEntity<>(bankAccountService.createBankAccount(bankAccountDto), HttpStatus.CREATED);
    }

    @GetMapping
    ResponseEntity<BankAccountDto> readAccountByAccountNumber(@RequestParam String accountNumber){
        return new ResponseEntity<>(bankAccountService.findByAccountNumber(accountNumber), HttpStatus.OK);
    }

    @GetMapping("/bankName")
    ResponseEntity<String> getBankName(@RequestParam String accountNumber){
        return new ResponseEntity<>(bankAccountService.getBankName(accountNumber), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ApiResponse> updateAccount(@RequestParam String accountNumber, @RequestBody BankAccountDto bankAccountDto) {
        return ResponseEntity.ok(bankAccountService.updateAccount(accountNumber, bankAccountDto));
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> accountBalance(@RequestParam String accountNumber) {
        return ResponseEntity.ok(bankAccountService.getBalance(accountNumber));
    }

    @GetMapping("/validate-account")
    public ResponseEntity<Boolean> validateAccount(@RequestParam String accountNumber, @RequestParam String bankName) {
        boolean isValid = bankAccountService.validateAccount(accountNumber, bankName);
        return ResponseEntity.ok(isValid);
    }
}
