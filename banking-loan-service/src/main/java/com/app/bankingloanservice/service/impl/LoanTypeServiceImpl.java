package com.app.bankingloanservice.service.impl;

import com.app.bankingloanservice.dto.LoanTypeDto;
import com.app.bankingloanservice.entity.LoanType;
import com.app.bankingloanservice.exception.LoanTypeNotFoundException;
import com.app.bankingloanservice.mapper.LoanTypeMapper;
import com.app.bankingloanservice.repository.LoanTypeRepository;
import com.app.bankingloanservice.service.LoanTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class LoanTypeServiceImpl implements LoanTypeService {

    private final LoanTypeRepository loanTypeRepository;

    private final LoanTypeMapper loanTypeMapper;

    @Override
    public LoanType getLoanTypeById(Long loanTypeId) {

        // Get LoanType, throw exception if not found
        return loanTypeRepository.findByLoanTypeId(loanTypeId)
                .orElseThrow(() -> new LoanTypeNotFoundException("Loan Type not found for ID: " + loanTypeId));
    }

    @Override
    public LoanTypeDto getLoanTypeDtoById(Long loanTypeId) {
        return loanTypeMapper.toDto(getLoanTypeById(loanTypeId));
    }
}
