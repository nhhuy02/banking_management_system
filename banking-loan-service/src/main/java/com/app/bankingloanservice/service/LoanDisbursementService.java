package com.app.bankingloanservice.service;

import org.springframework.transaction.annotation.Transactional;
// Add loan disbursement function
public interface LoanDisbursementService {
    @Transactional
    void disburseLoan(Long loanId);
}
