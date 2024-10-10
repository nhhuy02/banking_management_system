package com.ctv_it.customer_service.controller;

import com.ctv_it.customer_service.dto.CustomerDto;
import com.ctv_it.customer_service.dto.CustomerUpdateDto;
import com.ctv_it.customer_service.dto.GetAccountIdAndCustomerId;
import com.ctv_it.customer_service.response.ApiResponse;
import com.ctv_it.customer_service.service.CustomerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customer")
@Validated
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse<CustomerDto>> getCustomerByAccount(@PathVariable Long accountId) {
        logger.info("Get customer by account id {}", accountId);
        Optional<CustomerDto> customerDto = customerService.getCustomerByAccountId(accountId);
        if (customerDto.isPresent()) {
            ApiResponse<CustomerDto> response = new ApiResponse<>(HttpStatus.OK.value(),
                    "Customer found for account ID: " + accountId, true, customerDto.get());
            return ResponseEntity.ok(response);
        } else {
            logger.warn("Not found for account ID: {}", accountId);
            ApiResponse<CustomerDto> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(),
                    "Not found for account ID: " + accountId, false, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/byId/{Id}")
    public ResponseEntity<ApiResponse<CustomerDto>> getCustomerById(@PathVariable Long Id) {
        logger.info("Get customer by id {}", Id);
        Optional<CustomerDto> customerDto = customerService.getCustomerById(Id);
        if (customerDto.isPresent()) {
            ApiResponse<CustomerDto> response = new ApiResponse<>(HttpStatus.OK.value(),
                    "Customer found for customer ID: " + Id, true, customerDto.get());
            return ResponseEntity.ok(response);
        } else {
            logger.warn("Not found for customer ID: {}", Id);
            ApiResponse<CustomerDto> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(),
                    "Not found for customer ID: " + Id, false, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerDto>>> getAllCustomers() {
        List<CustomerDto> customers = customerService.getAllCustomers();
        ApiResponse<List<CustomerDto>> response = new ApiResponse<>(HttpStatus.OK.value(), "Customers retrieved", true,
                customers);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<ApiResponse<String>> updateCustomer(
            @PathVariable Long accountId,
            @Valid @RequestBody CustomerUpdateDto customerUpdateDto) {
        try {
            Optional<CustomerDto> updatedCustomerOptional = customerService.updateCustomer(accountId,
                    customerUpdateDto);

            if (updatedCustomerOptional.isPresent()) {
                String successMessage = "Customer with accountId " + accountId + " updated successfully.";
                logger.info(successMessage);
                ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(), successMessage, true, null);
                return ResponseEntity.ok(response);
            } else {
                String errorMessage = "Customer with accountId " + accountId + " not found.";
                logger.warn(errorMessage);
                ApiResponse<String> response = new ApiResponse<>(HttpStatus.NOT_FOUND.value(), errorMessage, false,
                        null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception ex) {
            String errorMessage = "An error occurred while updating the customer.";
            logger.warn("Error updating customer with accountId {}", accountId);
            ApiResponse<String> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage,
                    false, null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{accountId}/customer-info-for-account-service")
    public ResponseEntity<ApiResponse<GetAccountIdAndCustomerId>> getAccountAndCustomerId(
            @PathVariable Long accountId) {

        Optional<GetAccountIdAndCustomerId> data = customerService.getAccountIdAndCustomerId(accountId);

        if (data.isPresent()) {
            ApiResponse<GetAccountIdAndCustomerId> response = new ApiResponse<>(
                    HttpStatus.OK.value(),
                    "Customer found for account ID: " + accountId,
                    true,
                    data.get());
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<GetAccountIdAndCustomerId> response = new ApiResponse<>(
                    HttpStatus.NOT_FOUND.value(),
                    "Not found for account ID: " + accountId,
                    false,
                    null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

}
