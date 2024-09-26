package com.app.bankingloanservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class AuditModel {

    /**
     * The date and time when this collateral record was created.
     */
    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    /**
     * The date and time when this collateral record was last modified.
     */
    @Column(name = "last_modified_date")
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

//    /**
//     * The user who created this collateral record.
//     */
//    @CreatedBy
//    @Column(name = "created_by")
//    private String createdBy;
//
//    /**
//     * The user who last modified this collateral record.
//     */
//    @Column(name = "last_modified_by")
//    @LastModifiedBy
//    private String lastModifiedBy;

}
