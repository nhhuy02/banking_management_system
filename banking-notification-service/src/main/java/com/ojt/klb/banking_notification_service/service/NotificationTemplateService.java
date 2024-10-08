package com.ojt.klb.banking_notification_service.service;

import com.ojt.klb.banking_notification_service.dto.NotificationTemplateRequest;
import com.ojt.klb.banking_notification_service.dto.response.Response;
import com.ojt.klb.banking_notification_service.entity.NotificationTemplate;

public interface NotificationTemplateService {
    Response<Long> createOrUpdateNotificationTemplate(Long id, NotificationTemplateRequest request);
    Response<NotificationTemplate> findNotification(Long id);
}
