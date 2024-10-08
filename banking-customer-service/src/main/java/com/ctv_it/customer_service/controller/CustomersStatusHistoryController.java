package com.ctv_it.customer_service.controller;

import com.ctv_it.customer_service.dto.CustomersStatusHistoryDto;
import com.ctv_it.customer_service.response.ApiResponse;
import com.ctv_it.customer_service.service.CustomersStatusHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customer/status-history")
public class CustomersStatusHistoryController {

    private static final Logger logger = LoggerFactory.getLogger(CustomersStatusHistoryController.class);

    private final CustomersStatusHistoryService customersStatusHistoryService;

    public CustomersStatusHistoryController(CustomersStatusHistoryService customersStatusHistoryService) {
        this.customersStatusHistoryService = customersStatusHistoryService;
    }

    @GetMapping("/{customerId}/latest")
    public ResponseEntity<ApiResponse<CustomersStatusHistoryDto>> getLatestStatusByCustomerId(@PathVariable Long customerId) {
        logger.info("Received request to get latest status for customer ID: {}", customerId);
        Optional<CustomersStatusHistoryDto> dto = customersStatusHistoryService.getLatestStatusByCustomerId(customerId);
        if (dto.isPresent()) {
            ApiResponse<CustomersStatusHistoryDto> response = new ApiResponse<>(HttpStatus.OK.value(), "Latest status found", true, dto.get());
            return ResponseEntity.ok(response);
        } else {
            logger.warn("No latest status found for customer ID: {}", customerId);
            ApiResponse<CustomersStatusHistoryDto> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "No latest status found for customer ID: " + customerId, false, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/{customerId}/all")
    public ResponseEntity<ApiResponse<List<CustomersStatusHistoryDto>>> getAllStatusByCustomerId(@PathVariable Long customerId) {
        logger.info("Received request to get all statuses for customer ID: {}", customerId);
        Optional<List<CustomersStatusHistoryDto>> dto = customersStatusHistoryService.getAllStatusByCustomerId(customerId);
        if (dto.isPresent() && !dto.get().isEmpty()) {
            ApiResponse<List<CustomersStatusHistoryDto>> response = new ApiResponse<>(HttpStatus.OK.value(), "Statuses found", true, dto.get());
            return ResponseEntity.ok(response);
        } else {
            logger.warn("No statuses found for customer ID: {}", customerId);
            ApiResponse<List<CustomersStatusHistoryDto>> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), "No statuses found for customer ID: " + customerId, false, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
