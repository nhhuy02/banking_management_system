package com.app.bankingloanservice.controller;

import com.app.bankingloanservice.dto.*;
import com.app.bankingloanservice.service.LoanApplicationService;
import com.app.bankingloanservice.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing loan applications.
 */
@RestController
@RequestMapping("/api/v1/loan-service/loan-applications")
@AllArgsConstructor
@Tag(name = "Loan Application Controller", description = "APIs related to Loan Application operations")
@Slf4j
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    private final LoanService loanService;

    /**
     * Endpoint to register a new loan application.
     *
     * @param loanApplicationRequest DTO object representing the loan application details.
     * @return ResponseEntity containing ApiResponseWrapper with LoanApplicationResponse and HTTP status.
     */
    @Operation(
            summary = "Create a new loan application",
            description = "Registers a new loan application",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Loan application request body",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoanApplicationRequest.class),
                            examples = @ExampleObject(
                                    name = "Full Loan Application Example",
                                    summary = "Loan Application with all required fields",
                                    value = """
                                            {
                                              "accountId": 12345,
                                              "accountId": 98765,
                                              "monthlyIncome": 20000000,
                                              "occupation": "Software Engineer",
                                              "loanTypeId": 1,
                                              "desiredLoanAmount": 50000000,
                                              "desiredLoanTermMonths": 12,
                                              "repaymentMethod": "EQUAL_INSTALLMENTS",
                                              "desiredDisbursementDate": "2024-10-01",
                                              "interestRateType": "FIXED",
                                              "loanPurpose": "Home Renovation",
                                              "collateralRequest": {
                                                "collateralType": "string",
                                                "collateralValue": 0,
                                                "description": "string",
                                                "status": "ACTIVE",
                                                "reclaimDate": "2024-09-26",
                                                "reasonForReclamation": "string",
                                                "releaseDate": "2024-09-26"
                                              }
                                            }
                                            """
                            )
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Loan application created successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseWrapper.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid input data",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseWrapper.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseWrapper.class)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<ApiResponseWrapper<LoanApplicationResponse>> createLoanApplication(
            @Parameter(description = "Loan application request data", required = true)
            @Valid @RequestBody LoanApplicationRequest loanApplicationRequest) {

        // Create loan application and return the response
        LoanApplicationResponse createdLoanApplication = loanApplicationService.createLoanApplication(loanApplicationRequest);

        ApiResponseWrapper<LoanApplicationResponse> response = new ApiResponseWrapper<>(
                HttpStatus.CREATED.value(),
                true,
                "Created Loan Application!",
                createdLoanApplication
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    /**
     * Endpoint to retrieve a loan application by ID.
     *
     * @param loanApplicationId ID of the loan application to retrieve.
     * @return ResponseEntity containing ApiResponseWrapper with LoanApplicationResponse and HTTP status.
     */
    @Operation(
            summary = "Get a loan application by ID",
            description = "Retrieves a loan application based on the provided ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Loan application retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseWrapper.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Loan application not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseWrapper.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseWrapper.class)
                            )
                    )
            }
    )
    @GetMapping("/{loanApplicationId}")
    public ResponseEntity<ApiResponseWrapper<LoanApplicationResponse>> getLoanApplicationById(
            @Parameter(description = "ID of the loan application", required = true)
            @PathVariable Long loanApplicationId) {

        LoanApplicationResponse loanApplicationResponse = loanApplicationService.getResponseDtoById(loanApplicationId);

        ApiResponseWrapper<LoanApplicationResponse> response = new ApiResponseWrapper<>(
                HttpStatus.OK.value(),
                true,
                "Retrieved Loan Application!",
                loanApplicationResponse
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Endpoint to retrieve loan applications by accountId.
     *
     * @param accountId The ID of the account.
     * @param pageable  Pagination information.
     * @return ResponseEntity containing ApiResponseWrapper with a page of LoanApplicationResponse and HTTP status.
     */
    @Operation(
            summary = "Get Loan Applications by Account ID",
            description = "Retrieves loan applications for a specific account based on accountId with pagination support.",
            parameters = {
                    @Parameter(name = "accountId", description = "The ID of the account", required = true, example = "12345"),
                    @Parameter(name = "page", description = "Current page (0-based)", example = "0"),
                    @Parameter(name = "size", description = "Page size", example = "10"),
                    @Parameter(name = "sort", description = "Sort the result by a specific field, e.g.: loanDate,desc", example = "submissionDate,desc")
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Loan applications retrieved successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseWrapper.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "204",
                            description = "No loan applications found for the given accountId",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid accountId provided",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseWrapper.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseWrapper.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<ApiResponseWrapper<Page<LoanApplicationResponse>>> getLoanApplicationsByAccountId(
            @Parameter(description = "The ID of the account to fetch loan applications", required = true)
            @RequestParam @Min(value = 1, message = "accountId must be a positive number") Long accountId,

            @ParameterObject Pageable pageable) {

        log.info("Received request to fetch loan applications for accountId: {}", accountId);

        Page<LoanApplicationResponse> loanApplications = loanApplicationService.getLoanApplicationsByAccountId(accountId, pageable);

        String message;

        if (loanApplications.isEmpty()) {
            log.warn("No loan applications found for accountId: {}", accountId);
            message = "No loan applications found for the given accountId.";
        } else {
            log.info("Successfully retrieved loan applications for accountId: {}", accountId);
            message = "Loan applications retrieved successfully.";
        }

        ApiResponseWrapper<Page<LoanApplicationResponse>> response = new ApiResponseWrapper<>(
                HttpStatus.OK.value(),
                true,
                message,
                loanApplications
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(
            summary = "Update status of a loan application",
            description = "Updates the status of a loan application based on the provided ID and status",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Loan application status updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseWrapper.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Loan application not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseWrapper.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid loan application status",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseWrapper.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseWrapper.class)
                            )
                    )
            }
    )
    @PatchMapping("/{applicationId}/status")
    public ResponseEntity<ApiResponseWrapper<LoanApplicationResponse>> updateStatus(
            @Parameter(description = "ID of the loan application to update status", required = true)
            @PathVariable Long applicationId,
            @RequestBody @Valid LoanApplicationStatusDto loanApplicationStatusDto) {

        LoanApplicationResponse updatedApplication = loanApplicationService.updateStatus(applicationId, loanApplicationStatusDto);

        ApiResponseWrapper<LoanApplicationResponse> response = new ApiResponseWrapper<>(
                HttpStatus.OK.value(),
                true,
                "Loan application status updated successfully!",
                updatedApplication
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Operation(
            summary = "Add additional documents to your loan application",
            description = "Add additional documents required for a loan application based on the provided ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Additional documents updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseWrapper.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Loan application not found",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseWrapper.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request data",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseWrapper.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ApiResponseWrapper.class)
                            )
                    )
            }
    )
    @PostMapping(value = "/{applicationId}/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponseWrapper<DocumentResponse>> uploadDocument(
            @PathVariable("applicationId") Long loanApplicationId,
            @ModelAttribute DocumentUploadRequest documentUploadRequest) {

        log.info("Uploading document for loan application with ID: {}", loanApplicationId);

        // Call the loan application service to create and link the document to the loan application
        DocumentResponse documentResponse = loanApplicationService.uploadLoanApplicationDocument(loanApplicationId, documentUploadRequest);

        // Create the API response wrapper
        ApiResponseWrapper<DocumentResponse> response = new ApiResponseWrapper<>(
                HttpStatus.CREATED.value(),
                true,
                "Document uploaded successfully!",
                documentResponse
        );

        // Return the response with HTTP status 201 CREATED
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Create a loan from a loan application ID.
     *
     * @param loanApplicationId the ID of the loan application to create the loan from
     * @return the created loan entity wrapped in ApiResponseWrapper
     */
    @PostMapping("/{loanApplicationId}/loans")
    @Operation(summary = "Create Loan from Loan Application", description = "Create a new loan based on a loan application ID.")
    public ResponseEntity<ApiResponseWrapper<LoanResponse>> createLoanFromApplication(
            @PathVariable Long loanApplicationId) {

        LoanResponse loanResponse = loanService.createLoanFromApplicationId(loanApplicationId);

        ApiResponseWrapper<LoanResponse> response = new ApiResponseWrapper<>(
                HttpStatus.CREATED.value(),
                true,
                "Loan created successfully from loan application.",
                loanResponse
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}

