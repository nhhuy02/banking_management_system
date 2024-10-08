package com.ojt.klb.controller;

import com.ojt.klb.model.request.FundTransferRequest;
import com.ojt.klb.model.request.InterFundTransferRequest;
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
    public ResponseEntity<FundTransferResponse> internalFundTransfer(@Valid @RequestBody FundTransferRequest fundTransferRequest){
        return new ResponseEntity<>(fundTransferService.internalTransfer(fundTransferRequest), HttpStatus.CREATED);
    }
    @PostMapping("/external")
    public ResponseEntity<FundTransferResponse> externalFundTransfer(@Valid @RequestBody InterFundTransferRequest interFundTransferRequest){
        return new ResponseEntity<>(fundTransferService.interTransfer(interFundTransferRequest), HttpStatus.CREATED);
    }

//    @GetMapping("/{referenceNumber}")
//    public ResponseEntity<FundTransferDto> getTransferDetailsFromReferenceNumber(@PathVariable String referenceNumber) {
//        return new ResponseEntity<>(fundTransferService.getTransferDetailsFromReferenceNumber(referenceNumber), HttpStatus.OK);
//    }
//    @GetMapping
//    public ResponseEntity<List<FundTransferDto>> getAllTransfersByAccountNumber(@RequestParam String accountNumber) {
//        return new ResponseEntity<>(fundTransferService.getAllTransferByAccountNumber(accountNumber), HttpStatus.OK);
//    }

}
