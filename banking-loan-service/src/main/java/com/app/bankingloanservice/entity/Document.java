package com.app.bankingloanservice.entity;

import com.app.bankingloanservice.constant.DocumentType;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a document entity in the banking loan service.
 * This entity stores information about documents related to loan applications and collaterals.
 */
@Entity
@Table(name = "document")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Document extends AuditModel {

    //The unique identifier for the document.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long documentId;

    //The loan application associated with this document.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_application_id")
    private LoanApplication loanApplication;

    //The type of the document.
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    //A description of the document.
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

}