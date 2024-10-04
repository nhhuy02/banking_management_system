package com.ojt.klb.controller;

import com.ojt.klb.model.request.UtilityPaymentRequest;
import com.ojt.klb.service.UtilityPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/utility-payment")
public class UtilityPaymentController {
    private final UtilityPaymentService utilityPaymentService;

    @GetMapping
    public ResponseEntity readPayments(Pageable pageable) {
        return ResponseEntity.ok(utilityPaymentService.readPayments(pageable));
    }

    @PostMapping
    public ResponseEntity processPayment(@RequestBody UtilityPaymentRequest paymentRequest) {
        return ResponseEntity.ok(utilityPaymentService.utilPayment(paymentRequest));
    }
}
