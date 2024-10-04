package com.app.bankingloanservice.service.impl;

import com.app.bankingloanservice.client.account.AccountClientService;
import com.app.bankingloanservice.client.account.dto.AccountDto;
import com.app.bankingloanservice.constant.ApplicationStatus;
import com.app.bankingloanservice.dto.*;
import com.app.bankingloanservice.dto.kafka.LoanApplicationProducer;
import com.app.bankingloanservice.entity.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private final AccountClientService accountClientService;
    private final KafkaTemplate<String, LoanApplicationProducer> kafkaTemplate;

    private static final String TOPIC = "loan_application";

    @Override
    public LoanApplicationResponse createLoanApplication(LoanApplicationRequest loanApplicationRequest) {
        log.info("LoanApplicationRequest: {}", loanApplicationRequest);

        // Convert DTO to Entity
        LoanApplication loanApplication = loanApplicationMapper.toEntity(loanApplicationRequest);

        // Get LoanType
        LoanType loanType = loanTypeService.getLoanTypeById(loanApplicationRequest.getLoanTypeId());
        loanApplication.setLoanType(loanType);
        log.debug("Retrieved LoanType: {}", loanType);

        // Check to see if there is any Collateral (if Loan Type requires it)
        if (loanType.getRequiresCollateral() && loanApplicationRequest.getCollateralRequest() == null) {
            log.error("Loan type requires collateral but none provided.");
            throw new InvalidLoanApplicationException("Loan type requires collateral but none provided.");
        }

        log.info("LoanApplication entity converted from DTO and LoanType updated: {}", loanApplication);
        // Pre-save Loan Application
        loanApplication = loanApplicationRepository.save(loanApplication);
        log.info("LoanApplication entity has been saved: {}", loanApplication);

        // Save LoanApplication's Collateral
        CollateralRequest collateralRequest = loanApplicationRequest.getCollateralRequest();
        collateralRequest.setLoanApplicationId(loanApplication.getLoanApplicationId());
        Collateral savedCollateral = collateralService.createCollateral(collateralRequest);
        loanApplication.setCollateral(savedCollateral);
        log.info("Collateral created successfully: {}", savedCollateral); // Fixed errors related to Loan Application, completed API Docs, added README.md

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
        LoanApplicationResponse loanApplicationResponse = generateLoanApplicationResponse(loanApplication);

        // Get Account info from Account Service:
        AccountDto accountInfo = accountClientService.getAccountInfoById(loanApplication.getAccountId());

        // Kafka producer
        LoanApplicationProducer loanApplicationProducer = new LoanApplicationProducer();
        loanApplicationProducer.setLoanApplicationId(loanApplicationResponse.getLoanApplicationId());
        loanApplicationProducer.setAmounts(BigDecimal.valueOf(loanApplicationResponse.getDesiredLoanAmount()));
        loanApplicationProducer.setLoanTermMonths(loanApplicationResponse.getDesiredLoanTermMonths());
        loanApplicationProducer.setStatus(String.valueOf(loanApplicationResponse.getApplicationStatus()));
        loanApplicationProducer.setReviewTimeDays(loanApplicationResponse.getLoanTypeResponse().getReviewTimeDays());
        loanApplicationProducer.setCustomerId(accountInfo.getCustomerId());
        loanApplicationProducer.setEmail(accountInfo.getEmail());
        loanApplicationProducer.setCustomerName(accountInfo.getFullName());
        loanApplicationProducer.setSubmissionDate(loanApplicationResponse.getSubmissionDate());
        kafkaTemplate.send(TOPIC, loanApplicationProducer);

        return loanApplicationResponse;
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
    public LoanApplicationResponse getResponseDtoById(Long loanApplicationId) {
        return generateLoanApplicationResponse(getEntityById(loanApplicationId));
    }

    @Override
    public Page<LoanApplicationResponse> getLoanApplicationsByAccountId(Long accountId, Pageable pageable) {
        log.debug("Fetching loan applications for accountId: {} with pagination: {}", accountId, pageable);

        Page<LoanApplication> loanApplications = loanApplicationRepository.findByAccountId(accountId, pageable);

        if (loanApplications.isEmpty()) {
            log.warn("No loan applications found for accountId: {}", accountId);
            // Returns a blank page but retains pagination information
            return Page.empty(pageable);
        }

        // Map loanApplication to LoanApplicationResponse
        Page<LoanApplicationResponse> responseDtos = loanApplications.map(this::generateLoanApplicationResponse);
        log.debug("Fetched {} loan applications for accountId: {}", responseDtos.getContent().size(), accountId);

        return responseDtos;
    }


    @Override
    public DocumentResponse uploadLoanApplicationDocument(Long loanApplicationId, DocumentUploadRequest documentUploadRequest) {
        log.debug("Uploading document for LoanApplication ID: {}", loanApplicationId);

        // Get LoanApplication from Id
        LoanApplication loanApplication = getEntityById(loanApplicationId);

        // Save Loan Application Document
        Document savedDocument = documentService.createDocument(documentUploadRequest);
        loanApplication.getDocuments().add(savedDocument);
        log.info("Successfully created application document: {}", savedDocument);
        loanApplicationRepository.save(loanApplication);

        return documentMapper.toResponse(savedDocument);
    }


    @Override
    public LoanApplicationResponse updateStatus(Long applicationId, LoanApplicationStatusDto loanApplicationStatusDto) {
        log.debug("Updating status for LoanApplication ID: {}", applicationId);

        // Fetch LoanApplication entity by ID
        LoanApplication loanApplication = getEntityById(applicationId);

        // Check and update expired status if necessary
        checkAndUpdateExpiredStatus(loanApplication);

        // Validate the status update based on current status
        ApplicationStatus newStatus = loanApplicationStatusDto.getApplicationStatus();
        if (newStatus == null) {
            log.error("Invalid loan application status: null");
            throw new InvalidLoanApplicationException("Loan application status cannot be null.");
        }

        // Validate the status transition
        validateStatusTransition(loanApplication, newStatus);

        // Check if this is a status transition from REVIEWING to APPROVED or REJECTED
        ApplicationStatus currentStatus = loanApplication.getApplicationStatus();
        if (currentStatus == ApplicationStatus.REVIEWING &&
                (newStatus == ApplicationStatus.APPROVED || newStatus == ApplicationStatus.REJECTED)) {
            // Set the reviewDate to the current date if the status is being approved or rejected
            loanApplication.setReviewDate(LocalDate.now());
            log.debug("Review date set to {} for LoanApplication ID: {}", LocalDate.now(), applicationId);
        }

        // Update the loan application status and review notes if provided
        loanApplication.setApplicationStatus(newStatus);
        loanApplication.setReviewNotes(loanApplicationStatusDto.getReviewNotes());

        // Save updated loan application
        LoanApplication savedApplication = loanApplicationRepository.save(loanApplication);
        log.info("Loan application with ID {} updated to status {}", applicationId, newStatus);

        // Return the updated response DTO
        return generateLoanApplicationResponse(savedApplication);
    }


    private void validateStatusTransition(LoanApplication loanApplication, ApplicationStatus newStatus) {
        ApplicationStatus currentStatus = loanApplication.getApplicationStatus();

        // Check the validity of the transition
        if (currentStatus == ApplicationStatus.PENDING) {
            if (newStatus != ApplicationStatus.REVIEWING && newStatus != ApplicationStatus.DOCUMENT_REQUIRED) {
                throw new InvalidLoanApplicationException("Can only move from PENDING to REVIEWING or DOCUMENT_REQUIRED.");
            }
        } else if (currentStatus == ApplicationStatus.REVIEWING) {
            // After reviewing, it can only be APPROVED or REJECTED
            if (newStatus != ApplicationStatus.APPROVED && newStatus != ApplicationStatus.REJECTED) {
                throw new InvalidLoanApplicationException("Can only move from REVIEWING to APPROVED or REJECTED.");
            }
        } else if (currentStatus == ApplicationStatus.DOCUMENT_REQUIRED) {
            // From DOCUMENT_REQUIRED, we can only go to REVIEWING
            if (newStatus != ApplicationStatus.REVIEWING) {
                throw new InvalidLoanApplicationException("Can only move from DOCUMENT_REQUIRED back to REVIEWING.");
            }
        } else if (currentStatus == ApplicationStatus.EXPIRED) {
            throw new InvalidLoanApplicationException("Cannot change status from EXPIRED.");
        }
    }


    // Helper method to check and update expired status
    private boolean checkAndUpdateExpiredStatus(LoanApplication application) {
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

    // Add Account information from Account Service:
    private LoanApplicationResponse generateLoanApplicationResponse(LoanApplication loanApplication) {

        // Map Loan Application to response:
        LoanApplicationResponse loanApplicationResponse = loanApplicationMapper.toResponse(loanApplication);

        // Get Account info from Account Service:
        AccountDto accountInfo = accountClientService.getAccountInfoById(loanApplication.getAccountId());

        // Set Account info:
        loanApplicationResponse.setCustomerFullName(accountInfo.getFullName());
        loanApplicationResponse.setContactPhone(accountInfo.getPhoneNumber());
        loanApplicationResponse.setContactEmail(accountInfo.getEmail());
        loanApplicationResponse.setAccountNumber(accountInfo.getAccountNumber());
        log.debug("Converted LoanApplicationResponse with Loan Application ID: {}", loanApplication.getLoanApplicationId());
        return loanApplicationResponse;
    }

}
