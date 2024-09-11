package com.ojt.klb.baking_notification_service.controller;

import com.ojt.klb.baking_notification_service.dto.Response.Response;
import com.ojt.klb.baking_notification_service.service.NotificationService;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "API quản lí thông báo")
public class NotificationController {
    private final String root = "api/v1/NotificationService";

    @Autowired
    private NotificationService notificationService;
    @GetMapping(value = {root +"/test"})
    public Response<String> testAPI(HttpServletRequest httpRequest){
        return new Response<String>().withData("test response");

    }
    @GetMapping(value = {root +"/testMail"})
    public Response<String> testMail(HttpServletRequest httpRequest){
        return new Response<String>().withData(notificationService.sendMail("tuanvy3042001@gmail.com","abc"));

    }
}
