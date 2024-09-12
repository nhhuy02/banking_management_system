package com.ojt.klb.baking_notification_service.service.impl;

import com.ojt.klb.baking_notification_service.core.Mapper.NotificationTemplateMapper;
import com.ojt.klb.baking_notification_service.dto.NotificationTemplateRequest;
import com.ojt.klb.baking_notification_service.dto.Response.ResponseMessage;
import com.ojt.klb.baking_notification_service.entity.NotificationTemplate;
import com.ojt.klb.baking_notification_service.repository.NotificationTemplateRepository;
import com.ojt.klb.baking_notification_service.service.NotificationTemplateService;
import org.apache.kafka.common.security.oauthbearer.internals.secured.ValidateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationTemplateImpl implements NotificationTemplateService {
    @Autowired
    private NotificationTemplateRepository repository;
    @Override
    public Long createOrUpdateNotificationTemplate(Long id, NotificationTemplateRequest request) {

        NotificationTemplate notificationTemplate = new NotificationTemplate();

        if (id == null) {

            notificationTemplate.setBodyTemplate(request.getBodyTemplate());
            notificationTemplate.setSubjectTemplate(request.getSubjectTemplate());
            notificationTemplate.setTemplateName(request.getTemplateName());
        } else {

            notificationTemplate = repository.findById(id).orElseThrow(() ->
                    new ValidateException(ResponseMessage.ERROR.statusCodeValue())
            );

            notificationTemplate.setBodyTemplate(request.getBodyTemplate());
            notificationTemplate.setSubjectTemplate(request.getSubjectTemplate());
            notificationTemplate.setTemplateName(request.getTemplateName());
        }
        notificationTemplate.setCreatedAt(LocalDateTime.now());
        repository.save(notificationTemplate);
        return notificationTemplate.getId();
    }
}
