package com.ojt.klb.baking_notification_service.repository;

import com.ojt.klb.baking_notification_service.entity.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
}
