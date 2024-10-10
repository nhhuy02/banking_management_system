package com.ojt.klb.controller;

import com.ojt.klb.model.request.UtilityPaymentRequest;
import com.ojt.klb.service.UtilityPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/utility-payment")
public class UtilityPaymentController {
    private final UtilityPaymentService utilityPaymentService;

    @PostMapping
    public ResponseEntity processPayment(@Valid @RequestBody UtilityPaymentRequest paymentRequest) {
        return ResponseEntity.ok(utilityPaymentService.utilPayment(paymentRequest));
    }
}
