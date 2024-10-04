package com.app.bankingloanservice.service;

import com.app.bankingloanservice.dto.LoanTypeRequest;
import com.app.bankingloanservice.dto.LoanTypeResponse;
import com.app.bankingloanservice.entity.LoanType;

import java.util.List;

public interface LoanTypeService {

    LoanType getLoanTypeById(Long loanTypeId);

    LoanTypeResponse getLoanTypeDtoById(Long loanTypeId);

    List<LoanTypeResponse> getAllLoanTypes();

    LoanTypeResponse createLoanType(LoanTypeRequest loanTypeRequest);
}
