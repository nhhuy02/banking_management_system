package com.app.bankingloanservice.service.impl;

import com.app.bankingloanservice.dto.LoanTypeDto;
import com.app.bankingloanservice.entity.LoanType;
import com.app.bankingloanservice.exception.LoanTypeNotFoundException;
import com.app.bankingloanservice.mapper.LoanTypeMapper;
import com.app.bankingloanservice.repository.LoanTypeRepository;
import com.app.bankingloanservice.service.LoanTypeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class LoanTypeServiceImpl implements LoanTypeService {

    private final LoanTypeRepository loanTypeRepository;
    private final LoanTypeMapper loanTypeMapper;

    @Override
    public LoanType getLoanTypeById(Long loanTypeId) {
        log.info("Fetching LoanType with ID: {}", loanTypeId); // Log fetching loan type

        // Get LoanType, throw exception if not found
        return loanTypeRepository.findByLoanTypeId(loanTypeId)
                .orElseThrow(() -> {
                    log.error("Loan Type not found for ID: {}", loanTypeId); // Log when not found
                    return new LoanTypeNotFoundException("Loan Type not found for ID: " + loanTypeId);
                });
    }

    @Override
    public LoanTypeDto getLoanTypeDtoById(Long loanTypeId) {
        log.info("Converting LoanType entity to DTO for ID: {}", loanTypeId); // Log conversion to DTO
        return loanTypeMapper.toDto(getLoanTypeById(loanTypeId));
    }

    @Override
    public List<LoanTypeDto> getAllLoanTypes() {
        log.info("Fetching all LoanTypes"); // Log fetching all loan types
        List<LoanType> loanTypeList = loanTypeRepository.findAll();

        log.info("Converting {} LoanType entities to DTOs", loanTypeList.size()); // Log number of loan types
        return loanTypeList.stream()
                .map(loanTypeMapper::toDto)
                .collect(Collectors.toList());
    }
}
