package com.ctv_it.customer_service.service.impl;

import com.ctv_it.customer_service.dto.KycDto;
import com.ctv_it.customer_service.dto.KycResponseDto;
import com.ctv_it.customer_service.mapper.KycMapper;
import com.ctv_it.customer_service.model.Customer;
import com.ctv_it.customer_service.model.Kyc;
import com.ctv_it.customer_service.repository.CustomerRepository;
import com.ctv_it.customer_service.repository.KycRepository;
import com.ctv_it.customer_service.service.KycService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class KycServiceImpl implements KycService {

    private static final Logger logger = LoggerFactory.getLogger(KycServiceImpl.class);


    private final KycRepository kycRepository;
    private final KycMapper kycMapper;
    private final CustomerRepository customerRepository;

    public KycServiceImpl(KycRepository kycRepository, KycMapper kycMapper, CustomerRepository customerRepository) {
        this.kycRepository = kycRepository;
        this.kycMapper = kycMapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public Optional<Kyc.VerificationStatus> getKycStatusById(Long id) {
        Optional<Kyc> kycOptional = kycRepository.findById(id);
        if (kycOptional.isPresent()) {
            Kyc kyc = kycOptional.get();
            logger.info("Status of kyc with ID: {} is {}", kyc.getId(), kyc.getVerificationStatus());
            return Optional.of(kyc.getVerificationStatus());
        } else {
            logger.warn("No KYC found for ID: {}", id);
            return Optional.empty();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public KycDto updateKycbyAccountId(Long customerId, KycDto updatedKycDto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with id: " + customerId));

        logger.info("CustomerId: {}, KycId: {}", customerId, customer.getKyc().getId());

        return kycRepository.findById(customer.getKyc().getId())
                .map(existingKyc -> updateKycDetails(existingKyc, updatedKycDto))
                .map(kycRepository::save)
                .map(kycMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("KYC not found with id: " + customer.getKyc().getId()));
    }

    private Kyc updateKycDetails(Kyc existingKyc, KycDto updatedKycDto) {
        if (updatedKycDto.getDocumentType() != null) {
            try {
                existingKyc.setDocumentType(Kyc.DocumentType.valueOf(updatedKycDto.getDocumentType()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid document type: " + updatedKycDto.getDocumentType());
            }
        }

        if (updatedKycDto.getDocumentNumber() != null) {
            if (kycRepository.existsByDocumentNumber(updatedKycDto.getDocumentNumber())) {
                throw new IllegalArgumentException("Document number already exists: " + updatedKycDto.getDocumentNumber());
            }
            existingKyc.setDocumentNumber(updatedKycDto.getDocumentNumber());
        }

        if (updatedKycDto.getDocumentType() != null && updatedKycDto.getDocumentNumber() != null) {
            existingKyc.setVerificationStatus(Kyc.VerificationStatus.verified);
            existingKyc.setVerificationDate(Instant.now());
        }

        existingKyc.setUpdatedAt(Instant.now());
        return existingKyc;
    }

    @Override
    public Optional<KycResponseDto> getKycByCustomerId(Long customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> customer.getKyc())
                .flatMap(kyc -> kycRepository.findById(kyc.getId()))
                .map(kycMapper::toKycResponseDto);
    }


    @Override
    public Kyc saveKyc(Kyc kyc) {
        return kycRepository.save(kyc);
    }
}
