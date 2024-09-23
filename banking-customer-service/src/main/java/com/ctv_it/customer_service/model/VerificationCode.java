package com.ctv_it.customer_service.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;
import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "verification_codes")
public class VerificationCode {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "code", nullable = false, length = 10)
    private String code;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "expires_at")
    private Instant expiresAt;

    @ColumnDefault("0")
    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "used_at")
    private Instant usedAt;
}
