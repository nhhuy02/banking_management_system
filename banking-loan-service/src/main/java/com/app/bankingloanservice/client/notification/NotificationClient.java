//package com.app.bankingloanservice.client.notification;
//
//import com.app.bankingloanservice.client.notification.dto.NotificationRequest;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//@FeignClient(name = "notification-service", url = "${app.notification-client.url}")
//public interface NotificationClient {
//
//    @PostMapping("/notifications")
//    void sendNotification(@RequestBody NotificationRequest notificationRequest);
//
//}