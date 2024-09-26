package com.ojt.klb.controller;

import com.ojt.klb.model.dto.AccountDto;
import com.ojt.klb.model.dto.AccountStatusUpdate;
import com.ojt.klb.model.external.TransactionResponse;
import com.ojt.klb.model.response.Response;
import com.ojt.klb.service.AccountService;
import jakarta.validation.Valid;
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
    public ResponseEntity<Response> createAccount(@Valid @RequestBody AccountDto accountDto) {

        Long data = accountService.createAccount(accountDto);
        return ResponseEntity.ok(new Response(200, "Created account successfully", true, data));
    }

    @PatchMapping
    public ResponseEntity<Response> updateAccountStatus(@Valid @RequestParam String accountNumber,
            @RequestBody AccountStatusUpdate accountStatusUpdate) {
        this.accountService.updateStatus(accountNumber, accountStatusUpdate);
        return ResponseEntity.ok(new Response(200, "Updated account status successfully", true));
    }

    @GetMapping
    public ResponseEntity<AccountDto> readAccountByAccountNumber(@RequestParam String accountNumber) {
        return ResponseEntity.ok(accountService.readAccountByAccountNumber(accountNumber));
    }

    @PutMapping
    public ResponseEntity<Response> updateAccount(@Valid @RequestParam String accountNumber, @RequestBody AccountDto accountDto) {
        this.accountService.updateAccount(accountNumber, accountDto);
        return ResponseEntity.ok(new Response(200, "Update account successfully", true));
    }

    @GetMapping("/balance")
    public ResponseEntity<Response> getAccountBalance(@RequestParam String accountNumber) {
        String data = accountService.getBalance(accountNumber);
        return ResponseEntity.ok(new Response(200, "Get balance successfully", true, data));
    }

    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<List<TransactionResponse>> getTransactionsFromAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getTransactionsFromAccountNumber(accountNumber));
    }

    @PutMapping("/closure")
    public ResponseEntity<Response> closeAccount(@RequestParam String accountNumber) {
        this.accountService.closeAccount(accountNumber);
        return ResponseEntity.ok(new Response(200, "Closed account successfully", true));
    }

//    @GetMapping("/{userId}")
//    public ResponseEntity<?> readAccountsByUserId(@PathVariable Long userId) {
//        List<AccountDto> data = accountService.readAccountsByUserId(userId);
//        return ResponseEntity.ok(new Response(200, "Get accounts by user successfully", true, data));
//    }

    @PutMapping("/{accountNumber}/amount")
    public ResponseEntity<Response> updateBalance(@PathVariable String accountNumber, @RequestParam BigDecimal amount) {
        this.accountService.updateBalance(accountNumber, amount);
        return ResponseEntity.ok(new Response(200, "Updated balance successfully", true));
    }

    @GetMapping("/all")
    public ResponseEntity<Response> findAllAccount() {
        List<AccountDto> data = accountService.readAllAccounts();
        return ResponseEntity.ok(new Response(200, "Get accounts by user successfully", true, data));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Response> deleteAccountByAccountNumber(@RequestParam String accountNumber) {
        accountService.deleteAccount(accountNumber);
        return ResponseEntity.ok(new Response(200, "Deleted account successfully", true));
    }

}
