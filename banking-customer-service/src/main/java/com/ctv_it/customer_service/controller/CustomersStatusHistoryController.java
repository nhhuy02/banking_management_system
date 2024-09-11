package com.ctv_it.customer_service.controller;

import com.ctv_it.customer_service.dto.CustomersStatusHistoryDto;
import com.ctv_it.customer_service.service.CustomersStatusHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customer/status-history")
public class CustomersStatusHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(CustomersStatusHistoryController.class);

    @Autowired
    private CustomersStatusHistoryService customersStatusHistoryService;

    @GetMapping("/{customerId}/latest")
    public ResponseEntity<?> getLatestStatusByCustomerId(@PathVariable Long customerId) {
        logger.info("Received request to get latest status for customer ID: {}", customerId);
        Optional<CustomersStatusHistoryDto> dto = customersStatusHistoryService.getLatestStatusByCustomerId(customerId);
        if (dto.isPresent()) {
            return ResponseEntity.ok(dto.get());
        } else {
            logger.warn("No latest status found for customer ID: {}", customerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No latest status found for customer ID: " + customerId);
        }
    }

    @GetMapping("/{customerId}/all")
    public ResponseEntity<?> getAllStatusByCustomerId(@PathVariable Long customerId) {
        logger.info("Received request to get all statuses for customer ID: {}", customerId);
        Optional<List<CustomersStatusHistoryDto>> dto = customersStatusHistoryService.getAllStatusByCustomerId(customerId);
        if (dto.isPresent() && !dto.get().isEmpty()) {
            return ResponseEntity.ok(dto.get());
        } else {
            logger.warn("No statuses found for customer ID: {}", customerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Can't find any statuses for customer ID: " + customerId);
        }
    }
}
