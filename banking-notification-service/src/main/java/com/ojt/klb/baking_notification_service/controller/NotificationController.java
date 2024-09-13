package com.ojt.klb.baking_notification_service.controller;

import com.ojt.klb.baking_notification_service.dto.NotificationTemplateRequest;
import com.ojt.klb.baking_notification_service.dto.Response.Response;
import com.ojt.klb.baking_notification_service.service.NotificationService;
import com.ojt.klb.baking_notification_service.service.NotificationTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "API quản lí thông báo")
public class NotificationController {
    private final String root = "api/v1/NotificationService";

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationTemplateService notificationTemplateService;
    @GetMapping(value = {root +"/test"})
    public Response<String> testAPI(HttpServletRequest httpRequest){
        return new Response<String>().withData("test response");

    }
    @GetMapping(value = {root +"/testMail"})
    public Response<String> testMail(HttpServletRequest httpRequest){
        return new Response<String>().withData(notificationService.sendMail("tuanvy3042001@gmail.com"));

    }
    @Operation(summary = "API thêm mới, cập nhật thông báo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @PostMapping(value = {root +"/createOrUpdateNotificationTemplate"})
    public Response<Long> createOrUpdateNotificationTemplate(HttpServletRequest httpRequest,
                                                             @RequestParam(required = false) Long notificationTemplateId,
                                                             @RequestBody NotificationTemplateRequest request){
        return new Response<Long>().withData(notificationTemplateService.createOrUpdateNotificationTemplate(notificationTemplateId,request));

    }
}
