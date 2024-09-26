package com.ojt.klb.controller;

import com.ojt.klb.model.dto.FundTransferDto;
import com.ojt.klb.model.request.FundTransferRequest;
import com.ojt.klb.model.response.FundTransferResponse;
import com.ojt.klb.service.FundTransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/fund_transfer")
public class FundTransferController {
    private final FundTransferService fundTransferService;

    @PostMapping
    public ResponseEntity<FundTransferResponse> fundTransfer(@Valid @RequestBody FundTransferRequest fundTransferRequest){
        return new ResponseEntity<>(fundTransferService.fundTransfer(fundTransferRequest), HttpStatus.CREATED);
    }

    @GetMapping("/{referenceNumber}")
    public ResponseEntity<FundTransferDto> getTransferDetailsFromReferenceId(@PathVariable String referenceNumber) {
        return new ResponseEntity<>(fundTransferService.getTransferDetailsFromReferenceNumber(referenceNumber), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<FundTransferDto>> getAllTransfersByAccountId(@RequestParam String accountNumber) {
        return new ResponseEntity<>(fundTransferService.getAllTransferByAccountNumber(accountNumber), HttpStatus.OK);
    }

}
