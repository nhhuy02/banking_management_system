package com.ojt.klb.external;

import com.ojt.klb.configuration.FeignClientConfiguration;
import com.ojt.klb.model.external.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@FeignClient(name = "banking-account-service", configuration = FeignClientConfiguration.class)
public interface AccountService {

    @GetMapping("/accounts")
    Optional<Account> readByAccountNumber(@RequestParam String accountNumber);

    @PutMapping("/accounts")
    Account updateAccount(@RequestParam String accountNumber, @RequestBody Account account);

//    @PutMapping("/{accountNumber}/amount")
//    void updateBalance(@PathVariable String accountNumber, BigDecimal amount);
}