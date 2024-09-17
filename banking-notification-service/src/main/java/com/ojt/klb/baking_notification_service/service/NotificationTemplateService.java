package com.ojt.klb.baking_notification_service.service;

import com.ojt.klb.baking_notification_service.dto.NotificationTemplateRequest;
import com.ojt.klb.baking_notification_service.dto.Response.Response;
import com.ojt.klb.baking_notification_service.entity.NotificationTemplate;

public interface NotificationTemplateService {
    Response<Long> createOrUpdateNotificationTemplate(Long id, NotificationTemplateRequest request);
    Response<NotificationTemplate> findNotification(Long id);
}
