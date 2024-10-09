package com.ojt.klb.banking_notification_service.service.impl;


import com.ojt.klb.banking_notification_service.dto.NotificationTemplateRequest;
import com.ojt.klb.banking_notification_service.dto.response.Response;
import com.ojt.klb.banking_notification_service.dto.response.ResponseMessage;
import com.ojt.klb.banking_notification_service.entity.NotificationTemplate;

import com.ojt.klb.banking_notification_service.repository.NotificationTemplateRepository;
import com.ojt.klb.banking_notification_service.service.NotificationTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationTemplateImpl implements NotificationTemplateService {
    @Autowired
    private NotificationTemplateRepository repository;
    @Override
    public Response<Long> createOrUpdateNotificationTemplate(Long id, NotificationTemplateRequest request) {
        Response<Long> response = new Response<>();
        NotificationTemplate notificationTemplate = new NotificationTemplate();

        if (id == null) {


            notificationTemplate.setSubjectTemplate(request.getSubjectTemplate());
            notificationTemplate.setTemplateName(request.getTemplateName());
        } else {

            notificationTemplate = repository.findById(id).orElse(null);
            if (notificationTemplate == null) {
                return response.withError(ResponseMessage.ERROR);
            } else {

                notificationTemplate.setSubjectTemplate(request.getSubjectTemplate());
                notificationTemplate.setTemplateName(request.getTemplateName());
            }
        }
        notificationTemplate.setCreatedAt(LocalDateTime.now());
        repository.save(notificationTemplate);
        return response.withData(notificationTemplate.getId());
    }

    @Override
    public Response<NotificationTemplate> findNotification(Long id) {
        Response<NotificationTemplate> response = new Response<>();
        NotificationTemplate notificationTemplate = repository.findById(id).orElse(null);
        return response.withData(notificationTemplate) ;
    }
}
