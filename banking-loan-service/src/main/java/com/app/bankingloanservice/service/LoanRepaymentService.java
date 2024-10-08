package com.app.bankingloanservice.service;

import com.app.bankingloanservice.dto.LoanRepaymentResponse;
import com.app.bankingloanservice.entity.Loan;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LoanRepaymentService {

    void createRepaymentSchedule(Loan loan);

    @Transactional
    LoanRepaymentResponse makeRepayment(Long loanId, Long repaymentId);

    List<LoanRepaymentResponse> getRepaymentSchedule(Long loanId);

    List<LoanRepaymentResponse> getAvailableLoanRepayments(Long accountId);
}