package com.app.bankingloanservice.service;

import com.app.bankingloanservice.dto.LoanTypeDto;
import com.app.bankingloanservice.entity.LoanType;

public interface LoanTypeService {

    public LoanType getLoanTypeById(Long loanTypeId);

    public LoanTypeDto getLoanTypeDtoById(Long loanTypeId);
}
