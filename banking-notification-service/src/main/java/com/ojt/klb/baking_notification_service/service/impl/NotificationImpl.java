package com.ojt.klb.baking_notification_service.service.impl;

import com.ojt.klb.baking_notification_service.service.MailService;
import com.ojt.klb.baking_notification_service.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationImpl implements NotificationService {

    @Autowired
    private MailService mailConfig;
    @Override
    public String sendMail(String email) {
        String subject ="test";
        String body = "test";
        return mailConfig.send(email, subject, body);
    }
}
