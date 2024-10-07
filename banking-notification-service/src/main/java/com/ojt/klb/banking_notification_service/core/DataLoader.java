package com.ojt.klb.banking_notification_service.core;

import com.ojt.klb.banking_notification_service.entity.NotificationTemplate;
import com.ojt.klb.banking_notification_service.repository.NotificationTemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private NotificationTemplateRepository repository;
    @Override
    public void run(String... args) throws Exception {
        loadData();
    }
    private void loadData() {

        if (repository.count() == 0) {
            List<NotificationTemplate> templates = Arrays.asList(
                    new NotificationTemplate("ADMIN", LocalDateTime.now(),"OTP", "Mã xác minh của bạn"),
                    new NotificationTemplate("ADMIN", LocalDateTime.now(),"PAYMENT RECEIPT", "Biên lai chuyển khoản"),
                    new NotificationTemplate("ADMIN", LocalDateTime.now(),"PAYMENT REMINDER", "Thanh toán khoản vay"),
                    new NotificationTemplate("ADMIN", LocalDateTime.now(),"REGISTER", "Mở tài khoản"),
                    new NotificationTemplate("ADMIN", LocalDateTime.now(),"BALANCE CHANGE", "Biến động số dư"),
                    new NotificationTemplate("ADMIN", LocalDateTime.now(),"LOAN APPLICATION", "Đăng ký khoản vay"),

                    new NotificationTemplate("ADMIN", LocalDateTime.now(),"LOAN PAYMENT OVERDUE", "Thanh toán khoản vay quá hạn"),
                    new NotificationTemplate("ADMIN", LocalDateTime.now(),"LOAN DISBURSEMENT", "Giải ngân khoản vay")
            );
            repository.saveAll(templates);
            log.info("import data!!!!!!!!!!!!!!!!!!!!!!");

        }
    }
}
