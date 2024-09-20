package com.ojt.klb.baking_notification_service.entity;

import com.ojt.klb.baking_notification_service.core.BaseEntity;
import com.ojt.klb.baking_notification_service.validation.annotation.ColumnComment;
import com.ojt.klb.baking_notification_service.validation.annotation.TableComment;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Data
@Table(name = "notification")
@TableComment("Thông báo")
public class Notification extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @ColumnComment("ID bảng")
    private Long id;

    @Column(name = "customer_id")
    @ColumnComment("ID khách hàng")
    private Long customerId;

    @Column(name = "send_date")
    @ColumnComment("Ngày gửi")
    private LocalDateTime sendDate;

    @Column(name = "notification_template_id")
    @ColumnComment("ID của mẫu thông báo")
    private Long notificationTemplateId;

    @Column(name = "content")
    @ColumnComment("Nội dung")
    private String content;


}
