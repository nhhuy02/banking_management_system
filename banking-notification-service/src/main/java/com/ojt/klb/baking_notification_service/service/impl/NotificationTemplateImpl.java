package com.ojt.klb.baking_notification_service.service.impl;

import com.ojt.klb.baking_notification_service.core.Mapper.NotificationTemplateMapper;
import com.ojt.klb.baking_notification_service.dto.NotificationTemplateRequest;
import com.ojt.klb.baking_notification_service.dto.Response.Response;
import com.ojt.klb.baking_notification_service.dto.Response.ResponseMessage;
import com.ojt.klb.baking_notification_service.entity.NotificationTemplate;
import com.ojt.klb.baking_notification_service.exception.ValidateException;
import com.ojt.klb.baking_notification_service.repository.NotificationTemplateRepository;
import com.ojt.klb.baking_notification_service.service.NotificationTemplateService;
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

            notificationTemplate.setBodyTemplate(request.getBodyTemplate());
            notificationTemplate.setSubjectTemplate(request.getSubjectTemplate());
            notificationTemplate.setTemplateName(request.getTemplateName());
        } else {

//            notificationTemplate = repository.findById(id).orElseThrow(() ->
//
//                    new ValidateException(ResponseMessage.ERROR)
//            );
            notificationTemplate = repository.findById(id).orElse(null);
            if (notificationTemplate == null) {
                return response.withError(ResponseMessage.ERROR);
            } else {
                notificationTemplate.setBodyTemplate(request.getBodyTemplate());
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
