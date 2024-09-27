package com.ctv_it.customer_service.service.impl;

import com.ctv_it.customer_service.dto.CustomerDto;
import com.ctv_it.customer_service.dto.CustomerUpdateDto;
import com.ctv_it.customer_service.dto.CustomersStatusHistoryDto;
import com.ctv_it.customer_service.dto.GetAccountIdAndCustomerId;
import com.ctv_it.customer_service.mapper.CustomerMapper;
import com.ctv_it.customer_service.model.Customer;
import com.ctv_it.customer_service.model.CustomersStatusHistory;
import com.ctv_it.customer_service.model.Kyc;
import com.ctv_it.customer_service.repository.CustomerRepository;
import com.ctv_it.customer_service.service.CustomerService;
import com.ctv_it.customer_service.service.CustomersStatusHistoryService;
import com.ctv_it.customer_service.service.KycService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);


    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final KycService kycService;
    private final CustomersStatusHistoryService customersStatusHistoryService;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper, KycService kycService, CustomersStatusHistoryService customersStatusHistoryService) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.kycService = kycService;
        this.customersStatusHistoryService = customersStatusHistoryService;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CustomerDto> getCustomerByAccountId(Long accountId) {
        logger.info("Fetching customer by accountId: {}", accountId);
        Optional<Customer> customerOptional = customerRepository.findByAccountId(accountId);

        if (customerOptional.isEmpty()) {
            logger.warn("xCustomer with accountId: {} not found", accountId);
            return Optional.empty();
        }

        Customer customer = customerOptional.get();
        logger.info("Fetched customer by Id: {}", customer.getId());
        CustomerDto customerDto = customerMapper.toDto(customer);

        populateAdditionalCustomerInfo(customerDto, customer);

        return Optional.of(customerDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<CustomerDto> getCustomerById(Long id) {
        logger.info("Fetching customer by Id: {}", id);

        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (customerOptional.isEmpty()) {
            logger.warn("Customer with Id: {} not found", id);
            return Optional.empty();
        }

        Customer customer = customerOptional.get();
        CustomerDto customerDto = customerMapper.toDto(customer);

        populateAdditionalCustomerInfo(customerDto, customer);

        return Optional.of(customerDto);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerDto> getAllCustomers() {
        List<CustomerDto> customerDtoList = new ArrayList<>();
        List<Customer> customers = customerRepository.findAll();

        for (Customer customer : customers) {
            CustomerDto dto = customerMapper.toDto(customer);

            populateAdditionalCustomerInfo(dto, customer);

            customerDtoList.add(dto);
        }

        return customerDtoList;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Optional<CustomerDto> updateCustomer(Long accountId, CustomerUpdateDto customerUpdateDto) {
        try {
            logger.info("Updating customer with accountId: {}", accountId);

            Optional<Customer> customerOptional = customerRepository.findByAccountId(accountId);
            if (customerOptional.isEmpty()) {
                logger.warn("Customer with accountId: {} not found", accountId);
                return Optional.empty();
            }

            Customer customer = customerOptional.get();
            customer.setUpdatedAt(Instant.now());
            customerMapper.updateCustomerFromDto(customerUpdateDto, customer);

            // Save customer with the updated KYC reference
            Customer updatedCustomer = customerRepository.save(customer);
            logger.info("Updated Customer with ID: {}", updatedCustomer.getId());

            // Reload the customer from the database to ensure we have the latest state
            updatedCustomer = customerRepository.findById(updatedCustomer.getId()).orElseThrow(() -> new RuntimeException("Customer not found after save"));
            logger.info("Customer updated successfully with accountId: {}", accountId);
            logger.info("Updated customer ID: {}", updatedCustomer.getId());

            // Create and save status history
            CustomersStatusHistory statusHistory = new CustomersStatusHistory();
            statusHistory.setCustomer(updatedCustomer);
            statusHistory.setStatus(CustomersStatusHistory.Status.active);

            // Log the customer ID before saving status history
            logger.info("Saving status history with customer ID: {}", statusHistory.getCustomer().getId());

            CustomersStatusHistory savedStatusHistory = customersStatusHistoryService.saveStatusHistory(statusHistory);
            logger.info("Saved status history with customer ID: {}", savedStatusHistory.getCustomer().getId());

            return Optional.of(customerMapper.toDto(updatedCustomer));
        } catch (Exception e) {
            logger.error("Error updating customer with accountId: {}", accountId, e);
            throw e; // Rethrow the exception to trigger rollback
        }
    }

    @Override
    public Optional<GetAccountIdAndCustomerId> getAccountIdAndCustomerId(Long accountId) {
        Optional<Customer> customerOptional = customerRepository.findByAccountId(accountId);
        if (customerOptional.isPresent()) {
            GetAccountIdAndCustomerId data = new GetAccountIdAndCustomerId();
            data.setCustomerId(customerOptional.get().getId());
            data.setAccountId(accountId);
            return Optional.of(data);
        } else {
            logger.warn("Not found: {}", accountId);
            return Optional.empty();
        }
    }

    private void populateAdditionalCustomerInfo(CustomerDto dto, Customer customer) {
        // Get status KYC
        Optional<Kyc.VerificationStatus> kycStatus = kycService.getKycStatusById(customer.getKyc().getId());
        kycStatus.ifPresent(dto::setKycStatus);

        // Get status customer
        Optional<CustomersStatusHistoryDto> latestStatus = customersStatusHistoryService.getLatestStatusByCustomerId(customer.getId());
        latestStatus.ifPresent(dto::setLatestStatus);
    }

}
