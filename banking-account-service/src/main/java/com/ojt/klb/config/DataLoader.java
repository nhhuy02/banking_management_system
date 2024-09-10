package com.ojt.klb.config;

import com.ojt.klb.model.Account;
import com.ojt.klb.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner demoData(AccountRepository accountRepository) {
        return (args) -> {
            // Kiểm tra nếu không có tài khoản nào trong cơ sở dữ liệu
            if (accountRepository.count() == 0) {
                // Tạo một số dữ liệu tài khoản mẫu
                Account account1 = new Account(null, 1L, "SAVINGS", new BigDecimal("1000.00"), "active");
                Account account2 = new Account(null, 2L, "CHECKING", new BigDecimal("2000.00"), "active");
                Account account3 = new Account(null, 3L, "CREDIT", new BigDecimal("3000.00"), "active");

                // Lưu dữ liệu vào cơ sở dữ liệu
                accountRepository.save(account1);
                accountRepository.save(account2);
                accountRepository.save(account3);
            }
        };
    }
}
