package com.ojt.klb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ojt.klb.model.Account;
import com.ojt.klb.request.AccountRequest;
import com.ojt.klb.response.AccountResponse;
import com.ojt.klb.service.AccountService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // Lấy danh sách tất cả tài khoản
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        List<AccountResponse> accountResponses = accounts.stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(accountResponses);
    }

    // Lấy thông tin tài khoản theo ID
    @GetMapping("/{accountId}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long accountId) {
        Account account = accountService.getAccountById(accountId);
        return ResponseEntity.ok(new AccountResponse(account));
    }

    // Mở tài khoản mới
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest request) {
        Account account = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AccountResponse(account));
    }

    // Cập nhật thông tin tài khoản
    @PutMapping("/{accountId}")
    public ResponseEntity<AccountResponse> updateAccount(@PathVariable Long accountId,
            @RequestBody AccountRequest request) {
        Account account = accountService.updateAccount(accountId, request);
        return ResponseEntity.ok(new AccountResponse(account));
    }

    // Đóng tài khoản
    @DeleteMapping("/{accountId}")
    public ResponseEntity<Void> closeAccount(@PathVariable Long accountId) {
        accountService.closeAccount(accountId);
        return ResponseEntity.noContent().build();
    }
}
