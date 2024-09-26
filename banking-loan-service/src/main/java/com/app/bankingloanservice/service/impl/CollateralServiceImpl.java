package com.app.bankingloanservice.service.impl;

import com.app.bankingloanservice.dto.CollateralDto;
import com.app.bankingloanservice.dto.DocumentResponseDto;
import com.app.bankingloanservice.dto.DocumentUploadDto;
import com.app.bankingloanservice.entity.Collateral;
import com.app.bankingloanservice.entity.Document;
import com.app.bankingloanservice.entity.LoanApplication;
import com.app.bankingloanservice.exception.*;
import com.app.bankingloanservice.mapper.CollateralMapper;
import com.app.bankingloanservice.mapper.DocumentMapper;
import com.app.bankingloanservice.repository.CollateralRepository;
import com.app.bankingloanservice.repository.LoanApplicationRepository;
import com.app.bankingloanservice.service.CollateralService;
import com.app.bankingloanservice.service.DocumentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CollateralServiceImpl implements CollateralService {

    private final CollateralRepository collateralRepository;
    private final LoanApplicationRepository loanApplicationRepository;
    private final DocumentService documentService;
    private final CollateralMapper collateralMapper;
    private final DocumentMapper documentMapper;

    @Override
    public Collateral createCollateral(CollateralDto collateralDto) {
        // Fetch LoanApplication
        LoanApplication loanApplication = loanApplicationRepository.findById(collateralDto.getLoanApplicationId())
                .orElseThrow(() -> {
                    log.error("Invalid loan application ID: {}", collateralDto.getLoanApplicationId());
                    return new InvalidLoanApplicationException("Invalid loan application ID");
                });

        // Map to Collateral entity and save
        Collateral collateral = collateralMapper.toEntity(collateralDto);
        collateral.setLoanApplication(loanApplication);

        log.info("Saving collateral for Loan Application ID: {}", loanApplication.getLoanApplicationId());
        return collateralRepository.save(collateral);
    }

    @Override
    public DocumentResponseDto uploadCollateralDocument(Long collateralId, DocumentUploadDto documentUploadDto) {
        // Fetch Collateral
        Collateral collateral = collateralRepository.findById(collateralId)
                .orElseThrow(() -> {
                    log.error("Cannot find Collateral with this ID: {}", collateralId);
                    return new CollateralNotFoundException("Cannot find Collateral with this ID");
                });

        // Log thông tin về collateral
        log.info("Found collateral with ID: {}", collateralId);

        // Check whether Loan Application Id matches Collateral and documentUploadDto
        if (!documentUploadDto.getLoanApplicationId().equals(collateral.getLoanApplication().getLoanApplicationId())) {
            log.error("Loan Application ID does not match: collateralId={}, loanApplicationId={}",
                    collateralId, documentUploadDto.getLoanApplicationId());
            throw new InvalidDocumentException("Loan Application ID does not match");
        }

        // Save Document and associate with the collateral
        try {
            Document document = documentService.createDocument(documentUploadDto);
            collateral.setDocument(document); // Assuming you want to associate the document with collateral
            collateralRepository.save(collateral); // Save the collateral with the document reference

            log.info("Document uploaded and associated with collateral ID: {}", collateralId);
            log.info("Uploaded Document Details: {}", document);

            return documentMapper.toResponseDto(document);

        } catch (Exception e) {
            log.error("Failed to upload collateral document: {}", e.getMessage());
            throw new FileStorageException("Failed to upload collateral document", e);
        }
    }
}
