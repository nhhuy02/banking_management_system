package com.ojt.klb.banking_notification_service.repository;

import com.ojt.klb.banking_notification_service.entity.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
    @Query("select t from NotificationTemplate t where t.templateName = :templateName " +
            "and t.status = com.ojt.klb.banking_notification_service.core.Status.ACTIVE"
    )
    NotificationTemplate  getByTemplateName(String templateName);

}
