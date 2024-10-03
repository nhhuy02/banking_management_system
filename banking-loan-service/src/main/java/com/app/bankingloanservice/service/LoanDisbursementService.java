package com.app.bankingloanservice.service;

import com.app.bankingloanservice.dto.LoanDisbursementResponse;
import org.springframework.transaction.annotation.Transactional;

public interface LoanDisbursementService {
    @Transactional
    LoanDisbursementResponse disburseLoan(Long loanId);
}
