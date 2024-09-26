package com.ojt.klb.banking_notification_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NotificationDTO {
    @Schema(description = "ID khách hàng")
    private Long customerId;
    @Schema(description = "Nội dung")
    private String bodyTemplate;
    @Schema(description = "Tiêu đề")
    private String subjectTemplate;
    @Schema(description = "Tên")
    private String templateName;
    @Schema(description = "Thời gian gửi")
    private LocalDateTime sendDate;

}
