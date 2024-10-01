package com.app.bankingloanservice.service;

import com.app.bankingloanservice.dto.LoanTypeDto;
import com.app.bankingloanservice.entity.LoanType;

import java.util.List;

public interface LoanTypeService {

    LoanType getLoanTypeById(Long loanTypeId);

    LoanTypeDto getLoanTypeDtoById(Long loanTypeId);

    List<LoanTypeDto> getAllLoanTypes();
}
