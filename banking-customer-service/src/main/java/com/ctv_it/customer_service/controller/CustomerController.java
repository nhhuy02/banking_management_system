package com.ctv_it.customer_service.controller;

import com.ctv_it.customer_service.dto.CustomerDto;
import com.ctv_it.customer_service.dto.CustomerUpdateDto;
import com.ctv_it.customer_service.service.CustomerService;
import jakarta.persistence.EntityNotFoundException;
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

    @Autowired
    private CustomerService customerService;

    @GetMapping("/{accountId}")
    public ResponseEntity<?> getCustomerByAccount(@PathVariable Long accountId) {
        logger.info("Get customer by account id {}", accountId);
        Optional<CustomerDto> customerDto = customerService.getCustomerByAccountId(accountId);
        if (customerDto.isPresent()) {
            return ResponseEntity.ok(customerDto.get());
        } else {
            logger.warn("Not found for account ID: {}", accountId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found found for account ID: " + accountId);

        }
    }


    @GetMapping("/byId/{Id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long Id) {
        logger.info("Get customer by id {}", Id);
        Optional<CustomerDto> customerDto = customerService.getCustomerById(Id);
        if (customerDto.isPresent()) {
            return ResponseEntity.ok(customerDto.get());
        } else {
            logger.warn("Not found for customer ID: {}", Id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found for customer ID: " + Id);
        }
    }

    @GetMapping
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        List<CustomerDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @PutMapping("/{accountId}")
    public ResponseEntity<String> updateCustomer(
            @PathVariable Long accountId,
            @Valid @RequestBody CustomerUpdateDto customerUpdateDto) {
        try {
            customerService.updateCustomer(accountId, customerUpdateDto);
            String successMessage = "Customer with accountId " + accountId + " updated successfully.";
            logger.info(successMessage);
            return ResponseEntity.ok(successMessage);
        } catch (EntityNotFoundException ex) {
            String errorMessage = "Customer with accountId " + accountId + " not found.";
            logger.warn(errorMessage);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        } catch (Exception ex) {
            String errorMessage = "An error occurred while updating the customer.";
            logger.warn("Error updating customer with accountId {}", accountId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
}
