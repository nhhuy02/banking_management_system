package com.app.bankingloanservice.service.impl;

import com.app.bankingloanservice.constant.ApplicationStatus;
import com.app.bankingloanservice.dto.*;
import com.app.bankingloanservice.entity.Collateral;
import com.app.bankingloanservice.entity.Document;
import com.app.bankingloanservice.entity.LoanApplication;
import com.app.bankingloanservice.entity.LoanType;
import com.app.bankingloanservice.exception.InvalidLoanApplicationException;
import com.app.bankingloanservice.exception.LoanApplicationNotFoundException;
import com.app.bankingloanservice.mapper.DocumentMapper;
import com.app.bankingloanservice.mapper.LoanApplicationMapper;
import com.app.bankingloanservice.repository.LoanApplicationRepository;
import com.app.bankingloanservice.service.CollateralService;
import com.app.bankingloanservice.service.DocumentService;
import com.app.bankingloanservice.service.LoanApplicationService;
import com.app.bankingloanservice.service.LoanTypeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class LoanApplicationServiceImpl implements LoanApplicationService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final CollateralService collateralService;
    private final DocumentService documentService;
    private final LoanTypeService loanTypeService;
    private final LoanApplicationMapper loanApplicationMapper;
    private final DocumentMapper documentMapper;

    @Override
    public LoanApplicationResponseDto createLoanApplication(LoanApplicationRequestDto loanApplicationRequestDto) {
        log.info("LoanApplicationRequestDto: {}", loanApplicationRequestDto);

        // Convert DTO to Entity
        LoanApplication loanApplication = loanApplicationMapper.toEntity(loanApplicationRequestDto);

        // Get LoanType
        LoanType loanType = loanTypeService.getLoanTypeById(loanApplicationRequestDto.getLoanTypeId());
        loanApplication.setLoanType(loanType);
        log.debug("Retrieved LoanType: {}", loanType);

        // Check to see if there is any Collateral (if Loan Type requires it)
        if (loanType.getRequiresCollateral() && loanApplication.getCollateral() != null) {
            log.error("Loan type requires collateral but none provided.");
            throw new InvalidLoanApplicationException("Loan type requires collateral but none provided.");
        }

        log.info("LoanApplication entity converted from DTO and LoanType updated: {}", loanApplication);
        // Pre-save Loan Application
        loanApplication = loanApplicationRepository.save(loanApplication);
        log.info("LoanApplication entity has been saved: {}", loanApplication);

        // Save LoanApplication's Collateral
        CollateralDto collateralDto = loanApplicationRequestDto.getCollateralDto();
        collateralDto.setLoanApplicationId(loanApplication.getLoanApplicationId());
        Collateral savedCollateral = collateralService.createCollateral(collateralDto);
        loanApplication.setCollateral(savedCollateral);
        log.info("Collateral created successfully: {}", savedCollateral);

        // Set application status to PENDING and submission date to the current date
        loanApplication.setApplicationStatus(ApplicationStatus.PENDING);
        loanApplication.setSubmissionDate(LocalDate.now());
        log.debug("Loan Application submitted with status PENDING and submission date: {}", loanApplication.getSubmissionDate());

        // Calculate the review due date based on the loan type's review time
        loanApplication.setReviewDueDate(
                loanApplication.getSubmissionDate().plusDays(
                        loanApplication.getLoanType().getReviewTimeDays()
                )
        );
        log.info("Setting review due date to: {}", loanApplication.getReviewDueDate());

        // Save the loanApplication after updating Collateral and Document
        loanApplication = loanApplicationRepository.save(loanApplication);
        log.debug("Saved LoanApplication entity: {}", loanApplication);

        // Create a DTO to return the saved Loan Application
        LoanApplicationResponseDto loanApplicationResponseDto = loanApplicationMapper.toResponseDto(loanApplication);
        log.debug("Converted LoanApplicationResponseDto: {}", loanApplicationResponseDto);

        return loanApplicationResponseDto;
    }

    @Override
    public LoanApplication getEntityById(Long applicationId) {
        log.debug("Fetching LoanApplication with ID: {}", applicationId);
        return loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> {
                    log.error("Loan Application not found for ID: {}", applicationId);
                    return new LoanApplicationNotFoundException("Loan Application not found for ID: " + applicationId);
                });
    }

    @Override
    public LoanApplicationResponseDto getResponseDtoById(Long loanApplicationId) {
        LoanApplication loanApplication = getEntityById(loanApplicationId);

        // Convert LoanApplication to LoanApplicationResponseDto and return
        LoanApplicationResponseDto responseDto = loanApplicationMapper.toResponseDto(loanApplication);
        log.debug("Converted LoanApplicationResponseDto: {}", responseDto);

        return responseDto;
    }

    @Override
    public DocumentResponseDto uploadLoanApplicationDocument(Long loanApplicationId, DocumentUploadDto documentUploadDto) {
        log.debug("Uploading document for LoanApplication ID: {}", loanApplicationId);

        // Get LoanApplication from Id
        LoanApplication loanApplication = getEntityById(loanApplicationId);

        // Save Loan Application Document
        Document savedDocument = documentService.createDocument(documentUploadDto);
        loanApplication.getDocuments().add(savedDocument);
        log.info("Successfully created application document: {}", savedDocument);
        loanApplicationRepository.save(loanApplication);

        return documentMapper.toResponseDto(savedDocument);
    }

    @Override
    public LoanApplicationResponseDto approveApplication(Long applicationId) {
        log.debug("Approving LoanApplication with ID: {}", applicationId);
        LoanApplication application = getEntityById(applicationId);

        if (application.getApplicationStatus() != ApplicationStatus.REVIEWING) {
            log.error("Cannot approve application not in REVIEWING status.");
            throw new InvalidLoanApplicationException("Can only approve applications that are in REVIEWING status.");
        }

        application.setApplicationStatus(ApplicationStatus.APPROVED);
        application.setReviewDate(LocalDate.now());

        LoanApplication savedApplication = loanApplicationRepository.save(application);
        log.info("Loan application with ID {} has been approved", applicationId);

        return loanApplicationMapper.toResponseDto(savedApplication);
    }

    @Override
    public LoanApplicationResponseDto rejectApplication(Long applicationId, String reason) {
        log.debug("Rejecting LoanApplication with ID: {} for reason: {}", applicationId, reason);
        LoanApplication application = getEntityById(applicationId);

        if (application.getApplicationStatus() != ApplicationStatus.REVIEWING) {
            log.error("Cannot reject application not in REVIEWING status.");
            throw new InvalidLoanApplicationException("Can only reject applications that are in REVIEWING status.");
        }

        application.setApplicationStatus(ApplicationStatus.REJECTED);
        application.setReviewNotes(reason);
        application.setReviewDate(LocalDate.now());

        LoanApplication savedApplication = loanApplicationRepository.save(application);
        log.info("Loan application with ID {} has been rejected for reason: {}", applicationId, reason);

        return loanApplicationMapper.toResponseDto(savedApplication);
    }

    @Override
    public LoanApplicationResponseDto requestAdditionalDocuments(Long applicationId, String additionalDocuments) {
        log.debug("Requesting additional documents for LoanApplication ID: {} with notes: {}", applicationId, additionalDocuments);
        LoanApplication application = getEntityById(applicationId);

        if (!isExpired(application)) {
            log.error("Cannot request additional documents for non-expired applications.");
            throw new InvalidLoanApplicationException("Can only request documents for applications that are not expired.");
        }

        application.setApplicationStatus(ApplicationStatus.DOCUMENT_REQUIRED);
        application.setReviewNotes(additionalDocuments);

        LoanApplication savedApplication = loanApplicationRepository.save(application);
        log.info("Requested additional documents for Loan application with ID: {}", applicationId);

        return loanApplicationMapper.toResponseDto(savedApplication);
    }

    @Override
    public LoanApplicationResponseDto startReview(Long applicationId) {
        log.debug("Starting review for LoanApplication ID: {}", applicationId);
        LoanApplication application = getEntityById(applicationId);

        if (application.getApplicationStatus() != ApplicationStatus.PENDING) {
            log.error("Cannot start review for application not in PENDING status.");
            throw new InvalidLoanApplicationException("Can only start review for applications in PENDING status.");
        }

        application.setApplicationStatus(ApplicationStatus.REVIEWING);

        LoanApplication savedApplication = loanApplicationRepository.save(application);
        log.info("Started review for Loan application with ID {}", applicationId);

        return loanApplicationMapper.toResponseDto(savedApplication);
    }

    // Helper method to check and update expired status
    private boolean isExpired(LoanApplication application) {
        if (application.getReviewDueDate().isBefore(LocalDate.now()) &&
                (application.getApplicationStatus() == ApplicationStatus.PENDING ||
                        application.getApplicationStatus() == ApplicationStatus.REVIEWING)) {
            application.setApplicationStatus(ApplicationStatus.EXPIRED);
            loanApplicationRepository.save(application);
            log.info("Loan application with ID {} has expired", application.getLoanApplicationId());
            return true;
        }
        return false;
    }
}
