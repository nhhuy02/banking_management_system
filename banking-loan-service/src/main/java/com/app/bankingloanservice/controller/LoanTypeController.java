package com.app.bankingloanservice.controller;

import com.app.bankingloanservice.dto.ApiResponseWrapper;
import com.app.bankingloanservice.dto.LoanTypeDto;
import com.app.bankingloanservice.service.LoanTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/loan-service/loan-types")
@RequiredArgsConstructor
public class LoanTypeController {

    private final LoanTypeService loanTypeService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseWrapper<LoanTypeDto>> getLoanTypeById(@PathVariable Long id) {
        LoanTypeDto loanTypeDto = loanTypeService.getLoanTypeDtoById(id);
        ApiResponseWrapper<LoanTypeDto> response = new ApiResponseWrapper<>(
                HttpStatus.OK.value(),
                true,
                "Loan type retrieved successfully.",
                loanTypeDto
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponseWrapper<List<LoanTypeDto>>> getAllLoanTypes() {
        List<LoanTypeDto> loanTypeList = loanTypeService.getAllLoanTypes();
        ApiResponseWrapper<List<LoanTypeDto>> response = new ApiResponseWrapper<>(
                HttpStatus.OK.value(),
                true,
                "All loan types retrieved successfully.",
                loanTypeList
        );
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
