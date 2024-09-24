package com.ojt.klb.banking_notification_service.core;

import com.ojt.klb.banking_notification_service.validation.annotation.ColumnComment;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@MappedSuperclass
@NoArgsConstructor
@Data
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @ColumnComment("ID bảng")
    private Long id;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    @ColumnComment("Ngày tạo")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    @ColumnComment("Ngày cập nhật")
    private LocalDateTime updatedAt;

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    @ColumnComment("Người tạo")
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by")
    @ColumnComment("Người cập nhật")
    private String updatedBy;

    @ColumnComment("Trạng thái")
    @Column(name = "status", nullable = false)
    @Convert(converter = Status.Converter.class)
    private Status status = Status.ACTIVE;
}
