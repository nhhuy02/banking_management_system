package com.ojt.klb.config;

import com.ojt.klb.model.Account;
import com.ojt.klb.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataLoader implements CommandLineRunner {

    private final AccountRepository accountRepository;

    public DataLoader(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadAccounts();
    }

    private void loadAccounts() {
        if (accountRepository.count() == 0) { // Check if accounts already exist
            Account account1 = new Account(
                    1L,
                    "CHECKING",
                    "USD",
                    "Branch1",
                    "0000001");
            account1.setAvailableBalance(new BigDecimal("1000.00"));
            accountRepository.save(account1);

            Account account2 = new Account(
                    2L,
                    "SAVINGS",
                    "USD",
                    "Branch2",
                    "0000002");
            account2.setAvailableBalance(new BigDecimal("2000.00"));
            accountRepository.save(account2);

            // Add more accounts as needed
        }
    }
}
