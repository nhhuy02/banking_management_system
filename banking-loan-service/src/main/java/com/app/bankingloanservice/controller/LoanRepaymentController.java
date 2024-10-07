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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * @param loanId    ID of the loan
     * @param page      Page number
     * @param size      Number of records per page
     * @param direction Sort in ascending or descending order
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
    public ResponseEntity<ApiResponseWrapper<Page<LoanRepaymentResponse>>> getRepaymentSchedule(
            @PathVariable Long loanId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "paymentDueDate") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        log.info("Fetching repayment schedule for loanId: {}, page: {}, size: {}, sortBy: {}, direction: {}",
                loanId, page, size, sortBy, direction);

        Page<LoanRepaymentResponse> repayments = loanRepaymentService.getRepaymentSchedule(loanId, page, size, sortBy, direction);

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