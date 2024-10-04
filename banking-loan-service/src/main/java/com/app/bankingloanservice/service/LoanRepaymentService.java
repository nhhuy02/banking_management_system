package com.app.bankingloanservice.service;

import com.app.bankingloanservice.dto.LoanRepaymentResponse;
import com.app.bankingloanservice.dto.RepaymentRequest;
import com.app.bankingloanservice.entity.Loan;
import org.springframework.data.domain.Page;

public interface LoanRepaymentService {

    void createRepaymentSchedule(Loan loan);

    void makeRepayment(Long loanId, Long repaymentId, RepaymentRequest repaymentRequest);

    Page<LoanRepaymentResponse> getRepaymentSchedule(Long loanId, int page, int size, String sortBy, String direction);
}