package com.app.bankingloanservice.controller;

import com.app.bankingloanservice.dto.ApiResponseWrapper;
import com.app.bankingloanservice.dto.LoanRequest;
import com.app.bankingloanservice.dto.LoanResponse;
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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loans")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Loan Controller", description = "APIs related to loan operations")
public class LoanController {

    private final LoanService loanService;

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
     * Get all loans by Customer ID with pagination.
     *
     * @param customerId the ID of the customer whose loans to retrieve
     * @param pageable   the pagination information
     * @return paginated list of loans wrapped in ApiResponseWrapper
     */
    @GetMapping
    @Operation(summary = "Get Loans by Customer ID", description = "Retrieve all loans for a specific customer with pagination support.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loans retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponseWrapper.class))),
            @ApiResponse(responseCode = "204", description = "No loans found for the given customer",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid customer ID",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Server error",
                    content = @Content)
    })
    public ResponseEntity<ApiResponseWrapper<Page<LoanResponse>>> getLoansByCustomerId(
            @Parameter(description = "ID of the customer to retrieve loans for", required = true)
            @RequestParam Long customerId,

            @Parameter(description = "Pagination information")
            @ParameterObject Pageable pageable) {

        log.info("Received request to fetch loans for customerId: {} with pageable: {}", customerId, pageable);

        Page<LoanResponse> loans = loanService.getLoansByCustomerId(customerId, pageable);

        String message;

        if (loans.isEmpty()) {
            log.warn("No loans found for customerId: {}", customerId);
            message = "No loans found for the given customerId.";
        } else {
            log.info("Successfully retrieved loans for customerId: {}", customerId);
            message = "Loans retrieved successfully.";
        }

        ApiResponseWrapper<Page<LoanResponse>> response = new ApiResponseWrapper<>(
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
}
