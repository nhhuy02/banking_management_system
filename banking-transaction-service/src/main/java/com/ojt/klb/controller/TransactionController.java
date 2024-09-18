package com.ojt.klb.controller;

import com.ojt.klb.model.request.InternalTransferRequest;
import com.ojt.klb.model.request.TransactionRequest;
import com.ojt.klb.model.response.Response;
import com.ojt.klb.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService service;

    @PostMapping(value = "/deposit")
    public ResponseEntity<?> handleDeposit(@RequestBody TransactionRequest request){
        Response data = service.handleDeposit(request.getAccountNumber(), request.getAmount());
        return ResponseEntity.ok(new Response(200, "You have successfully sent money", true, data));
    }

    @PostMapping(value = "/withdraw")
    public ResponseEntity<?> handleWithdraw(@RequestBody TransactionRequest request){
        Response data = service.handleWithdraw(request.getAccountNumber(), request.getAmount());
        return ResponseEntity.ok(new Response(200, "You have successfully withdrawn money", true, data));
    }

    @PostMapping(value = "/internal")
    public ResponseEntity<?> internalFundTransfer(@RequestBody InternalTransferRequest request){
        Response data = service.internalFundTransfer(request);
        return ResponseEntity.ok(new Response(200, "You have successfully transferred money", true, data));
    }
}
