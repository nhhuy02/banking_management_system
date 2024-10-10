package com.ctv_it.customer_service.service.impl;

import com.ctv_it.customer_service.dto.CustomerDto;
import com.ctv_it.customer_service.dto.CustomerUpdateDto;
import com.ctv_it.customer_service.dto.CustomersStatusHistoryDto;
import com.ctv_it.customer_service.dto.GetAccountIdAndCustomerId;
import com.ctv_it.customer_service.exception.CustomException;
import com.ctv_it.customer_service.mapper.CustomerMapper;
import com.ctv_it.customer_service.model.Customer;
import com.ctv_it.customer_service.model.CustomersStatusHistory;
import com.ctv_it.customer_service.model.Kyc;
import com.ctv_it.customer_service.repository.CustomerRepository;
import com.ctv_it.customer_service.service.CustomerService;
import com.ctv_it.customer_service.service.CustomersStatusHistoryService;
import com.ctv_it.customer_service.service.KycService;
import org.springframework.dao.DataIntegrityViolationException;
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
        customerDto.setCustomerId(customer.getId());
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

            Customer updatedCustomer = customerRepository.save(customer);
            logger.info("Updated Customer with ID: {}", updatedCustomer.getId());

            updatedCustomer = customerRepository.findById(updatedCustomer.getId()).orElseThrow(() -> new RuntimeException("Customer not found after save"));
            logger.info("Customer updated successfully with accountId: {}", accountId);
            logger.info("Updated customer ID: {}", updatedCustomer.getId());

            // Create and save status history
            CustomersStatusHistory statusHistory = new CustomersStatusHistory();
            statusHistory.setCustomer(updatedCustomer);
            statusHistory.setStatus(CustomersStatusHistory.Status.active);

            logger.info("Saving status history with customer ID: {}", statusHistory.getCustomer().getId());

            CustomersStatusHistory savedStatusHistory = customersStatusHistoryService.saveStatusHistory(statusHistory);
            logger.info("Saved status history with customer ID: {}", savedStatusHistory.getCustomer().getId());

            return Optional.of(customerMapper.toDto(updatedCustomer));
        } catch (Exception e) {
            logger.error("Error updating customer with accountId: {}", accountId, e);
            throw e;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createCustomer(CustomerDto customerDto) {
        try {
            Customer customer = customerMapper.toEntity(customerDto);
            customer.setAccountId(customerDto.getAccountId());
            customer.setPhoneNumber(customerDto.getPhoneNumber());
            customer.setCreatedAt(Instant.now());

            Kyc newKyc = new Kyc();
            newKyc.setVerificationStatus(Kyc.VerificationStatus.pending);
            newKyc.setCreatedAt(Instant.now());
            Kyc savedKyc = kycService.saveKyc(newKyc);
            logger.info("Saved KYC with ID: {}", savedKyc.getId());
            customer.setKyc(savedKyc);
            logger.info("Set Kyc with ID: {}", customer.getKyc().getId());
            customerRepository.save(customer);

            logger.info("Customer created successfully with account ID: {}", customer.getAccountId());

        } catch (DataIntegrityViolationException e) {
            logger.error("Failed to create customer due to data integrity violation. Account ID: {}, Phone: {}. Error: {}",
                    customerDto.getAccountId(), customerDto.getPhoneNumber(), e.getMessage());
            throw new CustomException("Data integrity violation. Please ensure unique account ID and phone number.", e);

        } catch (Exception e) {
            logger.error("Error occurred while creating customer with account ID: {}, phone number: {}. Error: {}",
                    customerDto.getAccountId(), customerDto.getPhoneNumber(), e.getMessage());
            throw new CustomException("An unexpected error occurred while creating the customer.", e);
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

    @Override
    public boolean checkDuplicateEmail(String email, Long customerId) {
        logger.info("Checking for duplicate email: {} for customerId: {}", email, customerId);

        Optional<Customer> customerOptional = customerRepository.findByEmailAndIdNot(email, customerId);

        logger.info("Found customer: {}", customerOptional);

        if (customerOptional.isPresent()) {
            logger.warn("Duplicate email found: {} for customerId: {}", email, customerId);
            return true;
        }
        return false;
    }

}
