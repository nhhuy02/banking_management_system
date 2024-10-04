package com.app.bankingloanservice.controller;

import com.app.bankingloanservice.dto.ApiResponseWrapper;
import com.app.bankingloanservice.dto.LoanTypeRequest;
import com.app.bankingloanservice.dto.LoanTypeResponse;
import com.app.bankingloanservice.service.LoanTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loan-service/loan-types")
@RequiredArgsConstructor
public class LoanTypeController {

    private final LoanTypeService loanTypeService;

    @Operation(summary = "Get Loan Type by ID", description = "Retrieve details of a loan type by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loan type retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponseWrapper.class))),
            @ApiResponse(responseCode = "404", description = "Loan type not found",
                    content = @Content(schema = @Schema(implementation = ApiResponseWrapper.class)))
    })
    @GetMapping("/{loan-type-id}")
    public ResponseEntity<ApiResponseWrapper<LoanTypeResponse>> getLoanTypeById(@PathVariable("loan-type-id") Long id) {
        LoanTypeResponse loanTypeResponse = loanTypeService.getLoanTypeDtoById(id);
        ApiResponseWrapper<LoanTypeResponse> response = new ApiResponseWrapper<>(
                HttpStatus.OK.value(),
                true,
                "Loan type retrieved successfully.",
                loanTypeResponse
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Get all Loan Types", description = "Retrieve all loan types.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All loan types retrieved successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponseWrapper.class)))
    })
    @GetMapping
    public ResponseEntity<ApiResponseWrapper<List<LoanTypeResponse>>> getAllLoanTypes() {
        List<LoanTypeResponse> loanTypeList = loanTypeService.getAllLoanTypes();
        ApiResponseWrapper<List<LoanTypeResponse>> response = new ApiResponseWrapper<>(
                HttpStatus.OK.value(),
                true,
                "All loan types retrieved successfully.",
                loanTypeList
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Create a new LoanType", description = "Create a new loan type with the given details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Loan type created successfully",
                    content = @Content(schema = @Schema(implementation = ApiResponseWrapper.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ApiResponseWrapper.class)))
    })
    @PostMapping
    public ResponseEntity<ApiResponseWrapper<LoanTypeResponse>> createLoanType(
            @Valid @RequestBody LoanTypeRequest loanTypeRequest) {
        LoanTypeResponse createdLoanType = loanTypeService.createLoanType(loanTypeRequest);
        ApiResponseWrapper<LoanTypeResponse> response = new ApiResponseWrapper<>(
                HttpStatus.CREATED.value(),
                true,
                "Loan type created successfully.",
                createdLoanType
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
