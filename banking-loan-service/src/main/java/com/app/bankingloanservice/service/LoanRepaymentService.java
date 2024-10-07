package com.app.bankingloanservice.service;

import com.app.bankingloanservice.dto.LoanRepaymentResponse;
import com.app.bankingloanservice.dto.RepaymentRequest;
import com.app.bankingloanservice.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface LoanRepaymentService {

    void createRepaymentSchedule(Loan loan);

    @Transactional
    LoanRepaymentResponse makeRepayment(Long loanId, Long repaymentId);

    Page<LoanRepaymentResponse> getRepaymentSchedule(Long loanId, int page, int size, String sortBy, String direction);
}