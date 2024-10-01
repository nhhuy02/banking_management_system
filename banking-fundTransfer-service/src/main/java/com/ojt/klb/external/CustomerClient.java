package com.ojt.klb.external;

import com.ojt.klb.model.dto.Customer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "banking-customer-service", url = "http://localhost:8082/api/v1/customer")
public interface CustomerClient {
    @GetMapping("/byAccountNumber/{accountNumber}")
    ResponseEntity<Customer> readByAccountNumber(@PathVariable Long accountNumber);
}

