package com.ctv_it.customer_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "kyc")
public class Kyc {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", columnDefinition = "ENUM('verified', 'pending') DEFAULT 'pending'")
    private VerificationStatus verificationStatus;

    @Column(name = "verification_date")
    private Instant verificationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", columnDefinition = "ENUM('id_card', 'passport', 'driver_license')")
    private DocumentType documentType;

    @Column(name = "document_number")
    private String documentNumber;

    @Column(name = "document_image_url")
    private String documentImageUrl;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

    public enum VerificationStatus {
        verified,
        pending
    }

    public enum DocumentType {
        id_card,
        passport,
        driver_license
    }
}
