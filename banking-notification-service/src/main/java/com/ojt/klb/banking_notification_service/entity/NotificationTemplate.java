package com.ojt.klb.banking_notification_service.entity;

import com.ojt.klb.banking_notification_service.core.BaseEntity;
import com.ojt.klb.banking_notification_service.validation.annotation.ColumnComment;
import com.ojt.klb.banking_notification_service.validation.annotation.TableComment;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "notification_template")
@TableComment("Mẫu thông báo")
public class NotificationTemplate extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @ColumnComment("ID bảng")
    private Long id;
    @Column(name = "template_name")
    @ColumnComment("Tên mẫu thông báo")
    private String templateName;
    @Column(name = "subject_template")
    @ColumnComment("Tiêu đề thông báo")
    private String subjectTemplate;

}
