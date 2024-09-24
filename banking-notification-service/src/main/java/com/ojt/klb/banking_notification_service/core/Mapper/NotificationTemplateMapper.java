package com.ojt.klb.banking_notification_service.core.Mapper;

import com.ojt.klb.banking_notification_service.dto.NotificationTemplateRequest;
import com.ojt.klb.banking_notification_service.entity.NotificationTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NotificationTemplateMapper {
    NotificationTemplateMapper INSTANCE = Mappers.getMapper(NotificationTemplateMapper.class);
    NotificationTemplate toEntity(NotificationTemplateRequest request);

}
