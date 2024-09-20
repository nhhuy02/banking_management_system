package com.ojt.klb.controller;

import com.ojt.klb.model.dto.AccountDto;
import com.ojt.klb.model.dto.AccountStatusUpdate;
import com.ojt.klb.model.dto.external.TransactionResponse;
import com.ojt.klb.model.dto.response.Response;
import com.ojt.klb.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody AccountDto accountDto) {

        Long data = accountService.createAccount(accountDto);
        return ResponseEntity.ok(new Response(200, "Created account successfully", true, data));
    }

    @PatchMapping
    public ResponseEntity<?> updateAccountStatus(@RequestParam String accountNumber,
            @RequestBody AccountStatusUpdate accountStatusUpdate) {
        this.accountService.updateStatus(accountNumber, accountStatusUpdate);
        return ResponseEntity.ok(new Response(200, "Updated account status successfully", true));
    }

    @GetMapping
    public ResponseEntity<?> readAccountByAccountNumber(@RequestParam String accountNumber) {
        var data = accountService.readAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(new Response(200, "Get account successfully", true, data));
    }

    @PutMapping
    public ResponseEntity<?> updateAccount(@RequestParam String accountNumber, @RequestBody AccountDto accountDto) {
        this.accountService.updateAccount(accountNumber, accountDto);
        return ResponseEntity.ok(new Response(200, "Update account successfully", true));
    }

    @GetMapping("/balance")
    public ResponseEntity<?> accountBalance(@RequestParam String accountNumber) {
        String data = accountService.getBalance(accountNumber);
        return ResponseEntity.ok(new Response(200, "Get balance successfully", true, data));
    }

    @GetMapping("/{accountId}/transactions")
    public ResponseEntity<?> getTransactionsFromAccountId(@PathVariable String accountId) {
        List<TransactionResponse> data = accountService.getTransactionsFromAccountId(accountId);
        return ResponseEntity.ok(new Response(200, "Get transaction successfully", true, data));
    }

    @PutMapping("/closure")
    public ResponseEntity<?> closeAccount(@RequestParam String accountNumber) {
        this.accountService.closeAccount(accountNumber);
        return ResponseEntity.ok(new Response(200, "Closed account successfully", true));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> readAccountsByUserId(@PathVariable Long userId) {
        List<AccountDto> data = accountService.readAccountsByUserId(userId);
        return ResponseEntity.ok(new Response(200, "Get accounts by user successfully", true, data));
    }

    @PutMapping("/{accountNumber}/amount")
    public ResponseEntity<?> updateBalance(@PathVariable String accountNumber, @RequestParam BigDecimal amount) {
        this.accountService.updateBalance(accountNumber, amount);
        return ResponseEntity.ok(new Response(200, "Updated balance successfully", true));
    }

}
