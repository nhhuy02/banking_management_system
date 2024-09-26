package com.app.bankingloanservice.controller;

import com.app.bankingloanservice.dto.ApiResponseWrapper;
import com.app.bankingloanservice.dto.LoanApplicationRequestDto;
import com.app.bankingloanservice.dto.LoanApplicationResponseDto;
import com.app.bankingloanservice.service.LoanApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Controller for managing loan applications.
 */
@RestController
@RequestMapping("/loan-applications")
@AllArgsConstructor
@Tag(name = "Loan Application Controller", description = "APIs related to Loan Application operations")
@Slf4j
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    /**
     * Endpoint to register a new loan application.
     *
     * @param loanApplicationRequestDto DTO object representing the loan application details.
     * @return ResponseEntity containing ApiResponseWrapper with LoanApplicationResponseDto and HTTP status.
     */
    @Operation(
            summary = "Create a new loan application",
            description = "Registers a new loan application",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Loan application request body",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoanApplicationRequestDto.class),
                            examples = @ExampleObject(
                                    name = "Full Loan Application Example",
                                    summary = "Loan Application with all required fields",
                                    value = """
                                            {
                                              "customerId": 12345,
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
                                              "collateralDto": {
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
    public ResponseEntity<ApiResponseWrapper<LoanApplicationResponseDto>> createLoanApplication(
            @Parameter(description = "Loan application request data", required = true)
            @Valid @RequestBody LoanApplicationRequestDto loanApplicationRequestDto) throws IOException {

        // Create loan application and return the response
        LoanApplicationResponseDto createdLoanApplication = loanApplicationService.createLoanApplication(loanApplicationRequestDto);

        ApiResponseWrapper<LoanApplicationResponseDto> response = new ApiResponseWrapper<>(
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
     * @return ResponseEntity containing ApiResponseWrapper with LoanApplicationResponseDto and HTTP status.
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
    public ResponseEntity<ApiResponseWrapper<LoanApplicationResponseDto>> getLoanApplicationById(
            @Parameter(description = "ID of the loan application", required = true)
            @PathVariable Long loanApplicationId) {

        LoanApplicationResponseDto loanApplicationResponseDto = loanApplicationService.getResponseDtoById(loanApplicationId);

        ApiResponseWrapper<LoanApplicationResponseDto> response = new ApiResponseWrapper<>(
                HttpStatus.OK.value(),
                true,
                "Retrieved Loan Application!",
                loanApplicationResponseDto
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Endpoint to approve a loan application.
     *
     * @param applicationId ID of the loan application to approve.
     * @return ResponseEntity containing ApiResponseWrapper with LoanApplicationResponseDto and HTTP status.
     */
    @Operation(
            summary = "Approve a loan application",
            description = "Approves a loan application based on the provided ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Loan application approved successfully",
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
    @PostMapping("/{applicationId}/approve")
    public ResponseEntity<ApiResponseWrapper<LoanApplicationResponseDto>> approveApplication(
            @Parameter(description = "ID of the loan application to approve", required = true)
            @PathVariable Long applicationId) {

        LoanApplicationResponseDto approvedApplication = loanApplicationService.approveApplication(applicationId);

        ApiResponseWrapper<LoanApplicationResponseDto> response = new ApiResponseWrapper<>(
                HttpStatus.OK.value(),
                true,
                "Loan application approved successfully!",
                approvedApplication
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint to reject a loan application.
     *
     * @param applicationId ID of the loan application to reject.
     * @param reason        Reason for rejecting the loan application.
     * @return ResponseEntity containing ApiResponseWrapper with LoanApplicationResponseDto and HTTP status.
     */
    @Operation(
            summary = "Reject a loan application",
            description = "Rejects a loan application based on the provided ID and reason",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Loan application rejected successfully",
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
    @PostMapping("/{applicationId}/reject")
    public ResponseEntity<ApiResponseWrapper<LoanApplicationResponseDto>> rejectApplication(
            @Parameter(description = "ID of the loan application to reject", required = true)
            @PathVariable Long applicationId,
            @Parameter(description = "Reason for rejecting the loan application", required = true)
            @RequestParam String reason) {

        LoanApplicationResponseDto rejectedApplication = loanApplicationService.rejectApplication(applicationId, reason);

        ApiResponseWrapper<LoanApplicationResponseDto> response = new ApiResponseWrapper<>(
                HttpStatus.OK.value(),
                true,
                "Loan application rejected successfully!",
                rejectedApplication
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint to request additional documents for a loan application.
     *
     * @param applicationId       ID of the loan application to request additional documents for.
     * @param additionalDocuments List of additional documents required.
     * @return ResponseEntity containing ApiResponseWrapper with LoanApplicationResponseDto and HTTP status.
     */
    @Operation(
            summary = "Request additional documents for a loan application",
            description = "Requests additional documents for a loan application based on the provided ID and document list",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Additional documents requested successfully",
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
    @PostMapping("/{applicationId}/request-documents")
    public ResponseEntity<ApiResponseWrapper<LoanApplicationResponseDto>> requestAdditionalDocuments(
            @Parameter(description = "ID of the loan application to request additional documents for", required = true)
            @PathVariable Long applicationId,
            @Parameter(description = "List of additional documents required", required = true)
            @RequestParam String additionalDocuments) {

        LoanApplicationResponseDto applicationWithDocumentsRequested = loanApplicationService.requestAdditionalDocuments(applicationId, additionalDocuments);

        ApiResponseWrapper<LoanApplicationResponseDto> response = new ApiResponseWrapper<>(
                HttpStatus.OK.value(),
                true,
                "Additional documents requested successfully!",
                applicationWithDocumentsRequested
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint to start the review process for a loan application.
     *
     * @param applicationId ID of the loan application to start the review for.
     * @return ResponseEntity containing ApiResponseWrapper with LoanApplicationResponseDto and HTTP status.
     */
    @Operation(
            summary = "Start review for a loan application",
            description = "Starts the review process for a loan application based on the provided ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Review started successfully",
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
    @PostMapping("/{applicationId}/start-review")
    public ResponseEntity<ApiResponseWrapper<LoanApplicationResponseDto>> startReview(
            @Parameter(description = "ID of the loan application to start the review for", required = true)
            @PathVariable Long applicationId) {

        LoanApplicationResponseDto applicationUnderReview = loanApplicationService.startReview(applicationId);

        ApiResponseWrapper<LoanApplicationResponseDto> response = new ApiResponseWrapper<>(
                HttpStatus.OK.value(),
                true,
                "Review started successfully!",
                applicationUnderReview
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
