package com.app.bankingloanservice.controller;

import com.app.bankingloanservice.dto.ApiResponseWrapper;
import com.app.bankingloanservice.dto.LoanRepaymentResponse;
import com.app.bankingloanservice.dto.RepaymentRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loan-service/{loanId}/repayments")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Loan Repayment Controller", description = "APIs related to loan repayment operations")
public class LoanRepaymentController {

    private final LoanRepaymentService loanRepaymentService;

    /**
     * Endpoint to view the repayment schedule of a loan.
     *
     * @param loanId   ID of the loan
     * @param pageable Pagination details
     * @return List of repayment schedules
     */
    @GetMapping
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
    public ResponseEntity<ApiResponseWrapper<Page<LoanRepaymentResponse>>> getRepaymentSchedule(
            @Parameter(description = "ID of the loan", required = true)
            @PathVariable Long loanId,

            @Parameter(description = "Pagination details")
            Pageable pageable) {

        log.info("Fetching repayment schedule for loanId: {}", loanId);
        Page<LoanRepaymentResponse> repayments = loanRepaymentService.getRepaymentSchedule(loanId, pageable);

        ApiResponseWrapper<Page<LoanRepaymentResponse>> response = new ApiResponseWrapper<>(
                HttpStatus.OK.value(),
                true,
                "Repayment schedule retrieved successfully.",
                repayments
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Endpoint to make a payment for a specific repayment schedule.
     *
     * @param loanId           ID of the loan
     * @param repaymentId      ID of the repayment schedule
     * @param repaymentRequest Payment data
     * @return Success message for repayment
     */
    @PostMapping("/{repaymentId}/pay")
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
    public ResponseEntity<ApiResponseWrapper<Void>> makeRepayment(
            @Parameter(description = "ID of the loan", required = true)
            @PathVariable Long loanId,

            @Parameter(description = "ID of the repayment schedule", required = true)
            @PathVariable Long repaymentId,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "RepaymentRequest contains payment details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RepaymentRequest.class)))
            @RequestBody RepaymentRequest repaymentRequest) {

        log.info("Making repayment for loanId: {}, repaymentId: {}", loanId, repaymentId);
        loanRepaymentService.makeRepayment(loanId, repaymentId, repaymentRequest);

        ApiResponseWrapper<Void> response = new ApiResponseWrapper<>(
                HttpStatus.OK.value(),
                true,
                "Repayment made successfully.",
                null
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
