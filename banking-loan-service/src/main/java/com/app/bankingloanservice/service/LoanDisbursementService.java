package com.app.bankingloanservice.service;

import org.springframework.transaction.annotation.Transactional;

public interface LoanDisbursementService {
    @Transactional
    void disburseLoan(Long loanId);
}
