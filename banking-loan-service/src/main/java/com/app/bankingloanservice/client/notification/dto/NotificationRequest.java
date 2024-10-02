package com.app.bankingloanservice.client.notification.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationRequest {
    private Long accountId;
    private String message;
}