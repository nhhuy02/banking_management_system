package com.ojt.klb.controller;

import com.ojt.klb.model.request.FundTransferRequest;
import com.ojt.klb.model.request.InterFundTransferRequest;
import com.ojt.klb.model.response.ApiResponse;
import com.ojt.klb.model.response.FundTransferResponse;
import com.ojt.klb.service.FundTransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/fund_transfer")
public class FundTransferController {
    private final FundTransferService fundTransferService;

    @PostMapping("/internal")
    public ResponseEntity<ApiResponse<FundTransferResponse>> internalFundTransfer(
            @Valid @RequestBody FundTransferRequest fundTransferRequest) {
        FundTransferResponse response = fundTransferService.internalTransfer(fundTransferRequest);
        ApiResponse<FundTransferResponse> apiResponse = new ApiResponse<>(HttpStatus.CREATED.value(),
                "Internal fund transfer successful", true, response);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PostMapping("/external")
    public ResponseEntity<ApiResponse<FundTransferResponse>> externalFundTransfer(
            @Valid @RequestBody InterFundTransferRequest interFundTransferRequest) {
        FundTransferResponse response = fundTransferService.interTransfer(interFundTransferRequest);
        ApiResponse<FundTransferResponse> apiResponse = new ApiResponse<>(HttpStatus.CREATED.value(),
                "External fund transfer successful", true, response);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    // Uncomment and wrap these in ApiResponse as well
    // @GetMapping("/{referenceNumber}")
    // public ResponseEntity<ApiResponse<FundTransferDto>>
    // getTransferDetailsFromReferenceNumber(@PathVariable String referenceNumber) {
    // FundTransferDto transferDto =
    // fundTransferService.getTransferDetailsFromReferenceNumber(referenceNumber);
    // ApiResponse<FundTransferDto> apiResponse = new
    // ApiResponse<>(HttpStatus.OK.value(), "Transfer details fetched successfully",
    // true, transferDto);
    // return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    // }

    // @GetMapping
    // public ResponseEntity<ApiResponse<List<FundTransferDto>>>
    // getAllTransfersByAccountNumber(@RequestParam String accountNumber) {
    // List<FundTransferDto> transfers =
    // fundTransferService.getAllTransferByAccountNumber(accountNumber);
    // ApiResponse<List<FundTransferDto>> apiResponse = new
    // ApiResponse<>(HttpStatus.OK.value(), "Transfers fetched successfully", true,
    // transfers);
    // return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    // }
}
