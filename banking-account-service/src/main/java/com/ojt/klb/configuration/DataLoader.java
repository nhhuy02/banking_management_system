//package com.ojt.klb.configuration;
//
//import com.ojt.klb.model.AccountStatus;
//import com.ojt.klb.model.AccountType;
//import com.ojt.klb.model.entity.Account;
//import com.ojt.klb.repository.AccountRepository;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.List;
//
//@Configuration
//public class DataLoader {
//
//    @Bean
//    public CommandLineRunner loadData(AccountRepository accountRepository) {
//        return args -> {
//            // Kiểm tra nếu có dữ liệu trong bảng Account
//            List<Account> existingAccounts = accountRepository.findAll();
//
//            if (existingAccounts.isEmpty()) {
//                // Nếu không có dữ liệu, tạo dữ liệu mẫu
//                Account account1 = Account.builder()
//                        .accountNumber("1234567890")
//                        .accountType(AccountType.SAVINGS_ACCOUNT)
//                        .accountStatus(AccountStatus.ACTIVE)
//                        .openingDate(LocalDate.now())
//                        .availableBalance(new BigDecimal("1000.00"))
////                        .userId(1L)
//                        .build();
//
//                Account account2 = Account.builder()
//                        .accountNumber("0987654321")
//                        .accountType(AccountType.FIXED_DEPOSIT)
//                        .accountStatus(AccountStatus.BLOCKED)
//                        .openingDate(LocalDate.now().minusDays(10))
//                        .availableBalance(new BigDecimal("500.00"))
////                        .userId(2L)
//                        .build();
//
//                // Lưu dữ liệu vào cơ sở dữ liệu
//                accountRepository.save(account1);
//                accountRepository.save(account2);
//            }
//        };
//    }
//}
