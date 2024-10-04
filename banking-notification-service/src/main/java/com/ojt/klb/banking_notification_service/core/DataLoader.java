package com.ojt.klb.banking_notification_service.core;

import com.ojt.klb.banking_notification_service.entity.NotificationTemplate;
import com.ojt.klb.banking_notification_service.repository.NotificationTemplateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
            NotificationTemplate notificationTemplate1 = new NotificationTemplate();
            notificationTemplate1.setStatus(Status.ACTIVE);
            notificationTemplate1.setCreatedAt(LocalDateTime.now());
            notificationTemplate1.setCreatedBy("ADMIN");
            notificationTemplate1.setSubjectTemplate("Mã xác minh của bạn");
            notificationTemplate1.setTemplateName("OTP");

            NotificationTemplate notificationTemplate2 = new NotificationTemplate();
            notificationTemplate2.setStatus(Status.ACTIVE);
            notificationTemplate2.setCreatedAt(LocalDateTime.now());
            notificationTemplate2.setCreatedBy("ADMIN");
            notificationTemplate2.setSubjectTemplate("Biên lai chuyển khoản");
            notificationTemplate2.setTemplateName("PAYMENT RECEIPT");

            NotificationTemplate notificationTemplate3 = new NotificationTemplate();
            notificationTemplate3.setStatus(Status.ACTIVE);
            notificationTemplate3.setCreatedAt(LocalDateTime.now());
            notificationTemplate3.setCreatedBy("ADMIN");
            notificationTemplate3.setSubjectTemplate("Nhắc nhở thanh toán");
            notificationTemplate3.setTemplateName("PAYMENT REMINDER");

            NotificationTemplate notificationTemplate4 = new NotificationTemplate();
            notificationTemplate4.setStatus(Status.ACTIVE);
            notificationTemplate4.setCreatedAt(LocalDateTime.now());
            notificationTemplate4.setCreatedBy("ADMIN");
            notificationTemplate4.setSubjectTemplate("Mở tài khoản");
            notificationTemplate4.setTemplateName("REGISTER");

            NotificationTemplate notificationTemplate5 = new NotificationTemplate();
            notificationTemplate5.setStatus(Status.ACTIVE);
            notificationTemplate5.setCreatedAt(LocalDateTime.now());
            notificationTemplate5.setCreatedBy("ADMIN");
            notificationTemplate5.setSubjectTemplate("Biến động số dư");
            notificationTemplate5.setTemplateName("BALANCE_CHANGE");

            NotificationTemplate notificationTemplate6 = new NotificationTemplate();
            notificationTemplate4.setStatus(Status.ACTIVE);
            notificationTemplate4.setCreatedAt(LocalDateTime.now());
            notificationTemplate4.setCreatedBy("ADMIN");
            notificationTemplate4.setSubjectTemplate("Đơn đăng ký khoản vay");
            notificationTemplate4.setTemplateName("LOAN APPLICATION");

            repository.save(notificationTemplate1);
            repository.save(notificationTemplate2);
            repository.save(notificationTemplate3);
            repository.save(notificationTemplate4);
            repository.save(notificationTemplate5);
            repository.save(notificationTemplate6);
            log.warn("import data example");

        }
    }
}
