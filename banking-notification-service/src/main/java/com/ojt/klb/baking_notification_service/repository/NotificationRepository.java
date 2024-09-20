package com.ojt.klb.baking_notification_service.repository;

import com.ojt.klb.baking_notification_service.dto.NotificationDTO;
import com.ojt.klb.baking_notification_service.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query(value = "select new com.ojt.klb.baking_notification_service.dto.NotificationDTO(n.customerId,n.content,nt.subjectTemplate,nt.templateName ,n.sendDate) " +
            "from Notification  n  join NotificationTemplate nt " +
            "on n.notificationTemplateId= nt.id" +
            " where  1 = 1 and n.customerId = :customerId")
    Page<NotificationDTO> findNotification(Long customerId,Pageable pageable);
}
