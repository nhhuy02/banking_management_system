package com.ojt.klb.baking_notification_service.service.impl;

import com.ojt.klb.baking_notification_service.dto.CustomerData;
import com.ojt.klb.baking_notification_service.dto.NotificationDTO;
import com.ojt.klb.baking_notification_service.dto.Response.ListResponse;
import com.ojt.klb.baking_notification_service.entity.NotificationTemplate;
import com.ojt.klb.baking_notification_service.repository.NotificationRepository;
import com.ojt.klb.baking_notification_service.repository.NotificationTemplateRepository;
import com.ojt.klb.baking_notification_service.service.MailService;
import com.ojt.klb.baking_notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationImpl implements NotificationService {
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private MailService mailConfig;
    @Autowired
    NotificationTemplateRepository notificationTemplateRepository;
    @Autowired
    NotificationRepository notificationRepository;

    @Override
    @KafkaListener(topics = "customer-otp-topic", groupId = "group_id")
    public String sendMail(CustomerData customerData) {
        String email = customerData.getEmail();
        NotificationTemplate notificationTemplate = notificationTemplateRepository.getByTemplateName("Đăng ký");
        String subject ="[NoReply-JD]"+ notificationTemplate.getSubjectTemplate();
        String body = notificationTemplate.getBodyTemplate();
//        if (email != null && !email.isEmpty()) {
//            notificationService.sendMail(email);
//        }
        Map<String, Object> variables = new HashMap<>();
        variables.put("customerName", customerData.getCustomerName());
        variables.put("verificationCode", customerData.getOtpCode());

        // Gửi email với template
        return mailConfig.send(email, subject, "register_verification_template", variables);

    }

    @Override
    public ListResponse<NotificationDTO> findByCustomerId(Long id, Pageable pageable) {
        Page<NotificationDTO> notificationDTOS = notificationRepository.findNotification(id,pageable);
        return new ListResponse<>(notificationDTOS);
    }
}
