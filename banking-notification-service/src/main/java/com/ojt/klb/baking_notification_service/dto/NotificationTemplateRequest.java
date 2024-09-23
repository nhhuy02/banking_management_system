package com.ojt.klb.baking_notification_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

public class NotificationTemplateRequest {
    @Schema(description = "Nội dung")
    private String bodyTemplate;
    @Schema(description = "Tiêu đề")
    private String subjectTemplate;
    @Schema(description = "Tên")
    private String templateName;
}
