package com.ojt.klb.banking_notification_service.controller;

import com.ojt.klb.banking_notification_service.dto.NotificationDTO;
import com.ojt.klb.banking_notification_service.dto.response.ListResponse;
import com.ojt.klb.banking_notification_service.dto.response.Response;
import com.ojt.klb.banking_notification_service.entity.NotificationTemplate;
import com.ojt.klb.banking_notification_service.service.NotificationService;
import com.ojt.klb.banking_notification_service.service.NotificationTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "API quản lí thông báo")
@RequestMapping("/api/v1/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;
    @Autowired
    private NotificationTemplateService notificationTemplateService;


//    @PostMapping("/createOrUpdateNotificationTemplate")
//    public Response<Long> createOrUpdateNotificationTemplate(HttpServletRequest httpRequest,
//                                                             @RequestParam(required = false) Long notificationTemplateId,
//                                                             @RequestBody NotificationTemplateRequest request){
//        Response<Long> response = notificationTemplateService.createOrUpdateNotificationTemplate(notificationTemplateId,request);
//        return  response;
//
//    }

    @PostMapping("/findById")
    public Response<NotificationTemplate> find(HttpServletRequest httpRequest,
                                               @RequestParam(required = true) Long notificationTemplateId
                                                                             ){
        Response<NotificationTemplate> response = notificationTemplateService.findNotification(notificationTemplateId);
        return  response;

    }

    @Operation(summary = "API truy vấn lịch sử thông báo theo từng customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @GetMapping(value = {"/getAllNotification"})
    public Response<ListResponse<NotificationDTO>> getAll(HttpServletRequest httpRequest,
                                                          @RequestParam(required = true) Long customerId,
                                                          @SortDefault.SortDefaults({@SortDefault(sort = "sendDate", direction = Sort.Direction.DESC)})
                                                              Pageable pageable
    ){

        return  new Response<ListResponse<NotificationDTO>>().withData(notificationService.findByCustomerId(customerId,pageable));

    }
}
