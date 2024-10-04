package com.app.bankingloanservice.controller;

import com.app.bankingloanservice.dto.ApiResponseWrapper;
import com.app.bankingloanservice.dto.LoanDisbursementResponse;
import com.app.bankingloanservice.dto.LoanRequest;
import com.app.bankingloanservice.dto.LoanResponse;
import com.app.bankingloanservice.service.LoanDisbursementService;
import com.app.bankingloanservice.service.LoanService;
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
@RequestMapping("/api/v1/loan-service/loans")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Loan Controller", description = "APIs related to loan operations")
public class LoanController {

    private final LoanService loanService;
    private final LoanDisbursementService loanDisbursementService;

    /**
     * Get loan by ID.
     *
     * @param loanId the ID of the loan to retrieve
     * @return the loan entity wrapped in ApiResponseWrapper
     */
    @GetMapping("/{loanId}")
    @Operation(summary = "Get Loan by ID", description = "Retrieve loan details by its ID.")
    public ResponseEntity<ApiResponseWrapper<LoanResponse>> getLoanById(@PathVariable Long loanId) {

        LoanResponse loanResponse = loanService.getLoanResponseDtoById(loanId);

        ApiResponseWrapper<LoanResponse> response = new ApiResponseWrapper<>(
                HttpStatus.OK.value(),
                true,
                "Loan retrieved successfully.",
                loanResponse
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Get all loans by Account ID without pagination.
     *
     * @param accountId the ID of the account whose loans to retrieve
     * @return list of loans wrapped in ApiResponseWrapper
     */
    @Operation(summary = "Get Loans by Account ID", description = "Retrieve all loans for a specific account without pagination.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loans retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseWrapper.class))),
            @ApiResponse(responseCode = "204", description = "No loans found for the given account",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid account ID",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<ApiResponseWrapper<List<LoanResponse>>> getLoansByAccountId(
            @Parameter(description = "ID of the account to retrieve loans for", required = true)
            @RequestParam Long accountId) {

        log.info("Received request to fetch loans for accountId: {}", accountId);

        List<LoanResponse> loans = loanService.getLoansByAccountId(accountId);

        String message;

        if (loans.isEmpty()) {
            log.warn("No loans found for accountId: {}", accountId);
            message = "No loans found for the given accountId.";
            return new ResponseEntity<>(new ApiResponseWrapper<>(
                    HttpStatus.NO_CONTENT.value(),
                    true,
                    message,
                    null), HttpStatus.NO_CONTENT);
        } else {
            log.info("Successfully retrieved loans for accountId: {}", accountId);
            message = "Loans retrieved successfully.";
        }

        ApiResponseWrapper<List<LoanResponse>> response = new ApiResponseWrapper<>(
                HttpStatus.OK.value(),
                true,
                message,
                loans
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**
     * Create a loan from LoanRequest.
     *
     * @param loanRequest the DTO containing loan details to create a new loan
     * @return the created loan entity wrapped in ApiResponseWrapper
     */
    @PostMapping
    @Operation(summary = "Create Loan", description = "Create a new loan using LoanRequest.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Loan created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoanResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    public ResponseEntity<ApiResponseWrapper<LoanResponse>> createLoan(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "LoanRequest containing the loan details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoanRequest.class)))
            @RequestBody LoanRequest loanRequest) {

        LoanResponse loanResponse = loanService.createLoan(loanRequest);

        ApiResponseWrapper<LoanResponse> response = new ApiResponseWrapper<>(
                HttpStatus.CREATED.value(),
                true,
                "Loan created successfully.",
                loanResponse
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


    /**
     * Endpoint for loan disbursement.
     *
     * @param loanId ID of the loan to be disbursed
     * @return ApiResponseWrapper with LoanDisbursementResponse
     */
    @PostMapping("/{loanId}/disburse")
    @Operation(summary = "Disburse Loan", description = "Disburse the loan with the given ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan disbursed successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoanDisbursementResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid loan ID or loan status",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Loan not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    public ResponseEntity<ApiResponseWrapper<LoanDisbursementResponse>> disburseLoan(
            @Parameter(description = "ID of the loan to be disbursed", required = true)
            @PathVariable Long loanId) {
        log.info("Received request to disburse loan with ID: {}", loanId);

        // Call the disbursement service to process the loan
        LoanDisbursementResponse loanDisbursementResponse = loanDisbursementService.disburseLoan(loanId);

        // Return the response wrapped in ApiResponseWrapper
        ApiResponseWrapper<LoanDisbursementResponse> response = new ApiResponseWrapper<>(
                HttpStatus.OK.value(),
                true,
                "Loan disbursed successfully.",
                loanDisbursementResponse
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
