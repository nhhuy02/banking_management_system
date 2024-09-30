package com.ctv_it.customer_service.service;

import com.ctv_it.customer_service.dto.CustomerDto;
import com.ctv_it.customer_service.dto.CustomerUpdateDto;
import com.ctv_it.customer_service.dto.GetAccountIdAndCustomerId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CustomerService {
    Optional<CustomerDto> getCustomerByAccountId(Long id);
    Optional<CustomerDto> getCustomerById(Long id);
    List<CustomerDto> getAllCustomers();
    Optional<CustomerDto> updateCustomer(Long accountId, CustomerUpdateDto customerUpdateDto);

    Optional<GetAccountIdAndCustomerId> getAccountIdAndCustomerId(Long accountId);
}
