package com.ojt.klb.baking_notification_service.service;

import com.ojt.klb.baking_notification_service.dto.consumer.CustomerData;
import com.ojt.klb.baking_notification_service.dto.NotificationDTO;
import com.ojt.klb.baking_notification_service.dto.Response.ListResponse;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    String sendMail(CustomerData customerData);
    ListResponse<NotificationDTO> findByCustomerId(Long id, Pageable pageable);
}
