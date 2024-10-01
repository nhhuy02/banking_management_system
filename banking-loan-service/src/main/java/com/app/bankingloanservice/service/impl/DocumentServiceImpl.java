package com.app.bankingloanservice.service.impl;

import com.app.bankingloanservice.dto.DocumentUploadRequest;
import com.app.bankingloanservice.entity.Document;
import com.app.bankingloanservice.entity.LoanApplication;
import com.app.bankingloanservice.exception.InvalidDocumentException;
import com.app.bankingloanservice.exception.InvalidLoanApplicationException;
import com.app.bankingloanservice.exception.FileStorageException;
import com.app.bankingloanservice.repository.DocumentRepository;
import com.app.bankingloanservice.repository.LoanApplicationRepository;
import com.app.bankingloanservice.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;
    private final LoanApplicationRepository loanApplicationRepository;

    @Value("${app.file.storage.directory}")
    private String fileStorageDirectory;

    @Override
    public Document createDocument(DocumentUploadRequest documentUploadRequest) {

        // Get the file from the DTO
        MultipartFile file = documentUploadRequest.getDocumentFile();

        // Validate input: check if file is present and not empty
        if (file == null || file.isEmpty()) {
            log.error("Document file is missing or empty");
            throw new InvalidDocumentException("Document data and file must not be null or empty");
        }

        // Fetch the associated LoanApplication using the ID from DocumentDto
        LoanApplication loanApplication = loanApplicationRepository.findById(documentUploadRequest.getLoanApplicationId())
                .orElseThrow(() -> {
                    log.error("Loan application not found for ID: {}", documentUploadRequest.getLoanApplicationId());
                    return new InvalidLoanApplicationException("Invalid loan application ID: " + documentUploadRequest.getLoanApplicationId());
                });

        // Create the storage directory if it does not exist
        File directory = new File(fileStorageDirectory);
        if (!directory.exists()) {
            boolean dirCreated = directory.mkdirs();
            if (!dirCreated) {
                log.error("Failed to create directory: {}", fileStorageDirectory);
                throw new FileStorageException("Could not create directory for file storage");
            }
            log.info("Directory created: {}", fileStorageDirectory);
        }

        // Clean the file name
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        // Generate a unique file name with timestamp to avoid name collision
        fileName = System.currentTimeMillis() + "_" + fileName;
        String filePath = Paths.get(fileStorageDirectory, fileName).toString();

        try {
            // Save the file to the designated folder
            file.transferTo(new File(filePath));
            log.info("File saved successfully at: {}", filePath);
        } catch (IOException e) {
            log.error("Failed to store file at: {}. Error: {}", filePath, e.getMessage());
            throw new FileStorageException("Failed to store file: " + fileName, e);
        }

        // Create the Document entity and populate its fields
        Document document = Document.builder()
                .loanApplication(loanApplication)
                .documentType(documentUploadRequest.getDocumentType())
                .fileName(fileName)
                .fileType(file.getContentType())
                .fileSize(file.getSize())
                .filePath(filePath)
                .description(documentUploadRequest.getDescription())
                .build();

        // Log before saving the document
        log.info("Saving document for loan application ID: {}", loanApplication.getLoanApplicationId());

        // Save the document to the database and return it
        return documentRepository.save(document);
    }
}
