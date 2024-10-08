package com.app.bankingloanservice.controller;

import com.app.bankingloanservice.dto.ApiResponseWrapper;
import com.app.bankingloanservice.dto.LoanRepaymentResponse;
import com.app.bankingloanservice.service.LoanRepaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loan-service")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Loan Repayment Controller", description = "APIs related to loan repayment operations")
public class LoanRepaymentController {

    private final LoanRepaymentService loanRepaymentService;

    /**
     * Endpoint to view the repayment schedule of a loan.
     *
     * @param loanId ID of the loan
     * @return List of repayment schedules
     */
    @GetMapping("/loans/{loanId}/repayments")
    @Operation(summary = "Get Repayment Schedule", description = "Retrieve the repayment schedule for a specific loan.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Repayment schedule retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Loan not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    public ResponseEntity<ApiResponseWrapper<List<LoanRepaymentResponse>>> getRepaymentSchedule(
            @PathVariable Long loanId) {

        log.info("Fetching repayment schedule for loanId: {}", loanId);

        // Get all repayments by loanId
        List<LoanRepaymentResponse> repayments = loanRepaymentService.getRepaymentSchedule(loanId);

        ApiResponseWrapper<List<LoanRepaymentResponse>> response = new ApiResponseWrapper<>(
                HttpStatus.OK.value(),
                true,
                "Repayment schedule retrieved successfully.",
                repayments
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/accounts/{accountId}/repayments/available")
    @Operation(summary = "Get Available Loan Repayments", description = "Retrieve all available loan repayments for a specific account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Available loan repayments retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseWrapper.class))),
            @ApiResponse(responseCode = "404", description = "No available repayments found for this account",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    public ResponseEntity<ApiResponseWrapper<List<LoanRepaymentResponse>>> getAvailableLoanRepayments(
            @Parameter(description = "ID of the account", required = true)
            @PathVariable Long accountId) {

        log.info("Fetching available loan repayments for accountId: {}", accountId);

        // Get all available repayments for the account
        List<LoanRepaymentResponse> repayments = loanRepaymentService.getAvailableLoanRepayments(accountId);

        // Check if repayments list is empty
        if (repayments.isEmpty()) {
            log.warn("No available loan repayments found for accountId: {}", accountId);
            ApiResponseWrapper<List<LoanRepaymentResponse>> emptyResponse = new ApiResponseWrapper<>(
                    HttpStatus.OK.value(),
                    false,
                    "No available loan repayments found for this account.",
                    null
            );
            return new ResponseEntity<>(emptyResponse, HttpStatus.OK);
        }

        // If repayments are found, return the data
        ApiResponseWrapper<List<LoanRepaymentResponse>> response = new ApiResponseWrapper<>(
                HttpStatus.OK.value(),
                true,
                "Available loan repayments retrieved successfully.",
                repayments
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Endpoint to make a payment for a specific repayment schedule.
     *
     * @param loanId      ID of the loan
     * @param repaymentId ID of the repayment schedule
     * @return Repayment details including transaction reference
     */
    @PostMapping("/loans/{loanId}/repayments/{repaymentId}/pay")
    @Operation(summary = "Make a Repayment", description = "Make a payment for a specific repayment schedule.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Repayment made successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "Invalid payment data",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Loan or Repayment not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    public ResponseEntity<ApiResponseWrapper<LoanRepaymentResponse>> makeRepayment(
            @Parameter(description = "ID of the loan", required = true)
            @PathVariable Long loanId,

            @Parameter(description = "ID of the repayment schedule", required = true)
            @PathVariable Long repaymentId) {

        log.info("Making repayment for loanId: {}, repaymentId: {}", loanId, repaymentId);

        // Process loan payments
        LoanRepaymentResponse repaymentResponse = loanRepaymentService.makeRepayment(loanId, repaymentId);

        // Return response
        ApiResponseWrapper<LoanRepaymentResponse> response = new ApiResponseWrapper<>(
                HttpStatus.OK.value(),
                true,
                "Repayment made successfully.",
                repaymentResponse
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}